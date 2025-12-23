import os
import psycopg2
from flask import Flask, request, jsonify
from flask_bcrypt import Bcrypt
import jwt
from datetime import datetime, timedelta
from functools import wraps
import logging
from flask_cors import CORS

logging.basicConfig(level=logging.INFO, format='%(asctime)s - %(levelname)s - %(message)s')

app = Flask(__name__)
bcrypt = Bcrypt(app)
CORS(app)

DB_NAME = os.getenv('DB_NAME', 'postgres')
DB_USER = os.getenv('DB_USER', 'postgres')
DB_PASSWORD = os.getenv('DB_PASSWORD', 'MotoG-60ssa')
DB_HOST = os.getenv('DB_HOST', 'db.qnbklsrqkgeimfkjpwim.supabase.co')
DB_PORT = os.getenv('DB_PORT', '5432')

SECRET_KEY = os.getenv('SECRET_KEY', 'OmaigaOmaigaOmaiga@123412341234')
app.config['SECRET_KEY'] = SECRET_KEY

def get_db_connection():
    try:
        conn = psycopg2.connect(
            dbname=DB_NAME,
            user=DB_USER,
            password=DB_PASSWORD,
            host=DB_HOST,
            port=DB_PORT
        )
        return conn
    except psycopg2.Error as e:
        logging.error(f"DB Connection Error: {e}")
        return None

def token_required(f):
    @wraps(f)
    def decorated(*args, **kwargs):
        token = request.headers.get('Authorization')
        if token:
            try:
                token = token.split(" ")[1]
                data = jwt.decode(token, app.config['SECRET_KEY'], algorithms=["HS256"])
                current_user_id = data['user_id']
            except jwt.ExpiredSignatureError:
                return jsonify({'message': 'Token expired.'}), 401
            except Exception as e:
                return jsonify({'message': 'Invalid token.', 'error': str(e)}), 401
        else:
            return jsonify({'message': 'Token is missing!'}), 401

        return f(current_user_id, *args, **kwargs)
    return decorated

@app.route('/login', methods=['POST'])
def login():
    data = request.get_json()
    username = data.get('username')
    password = data.get('password')

    if not username or not password:
        return jsonify({'message': 'Missing credentials'}), 400

    conn = get_db_connection()
    if conn is None:
        return jsonify({'message': 'Database connection error'}), 500

    try:
        with conn.cursor() as cur:
            cur.execute("SELECT id, password FROM users WHERE username = %s", (username,))
            user = cur.fetchone()
            if user and bcrypt.check_password_hash(user[1], password):
                token = jwt.encode({
                    'user_id': user[0],
                    'exp': datetime.utcnow() + timedelta(hours=1)
                }, app.config['SECRET_KEY'], algorithm="HS256")
                return jsonify({'message': 'Login successful', 'token': token}), 200
            else:
                return jsonify({'message': 'Invalid credentials'}), 401
    finally:
        conn.close()

@app.route('/tasks', methods=['GET'])
@token_required
def get_tasks(user_id):
    conn = get_db_connection()
    if conn is None:
        return jsonify({'message': 'Database connection error'}), 500
    try:
        with conn.cursor() as cur:
            cur.execute("SELECT id, name, category, is_selected, is_favorite FROM tasks WHERE user_id = %s", (user_id,))
            rows = cur.fetchall()
            tasks = [
                {'id': row[0], 'name': row[1], 'category': row[2], 'is_selected': row[3], 'is_favorite': row[4]}
                for row in rows
            ]
            return jsonify(tasks), 200
    finally:
        conn.close()

@app.route('/tasks', methods=['POST'])
@token_required
def create_task(user_id):
    data = request.get_json()
    name = data.get('name')
    category = data.get('category')
    is_selected = data.get('is_selected', False)
    is_favorite = data.get('is_favorite', False)

    if not name or not category:
        return jsonify({'message': 'Missing name or category'}), 400

    conn = get_db_connection()
    if conn is None:
        return jsonify({'message': 'Database connection error'}), 500
    try:
        with conn.cursor() as cur:
            cur.execute("""
                INSERT INTO tasks (user_id, name, category, is_selected, is_favorite)
                VALUES (%s, %s, %s, %s, %s) RETURNING id
            """, (user_id, name, category, is_selected, is_favorite))
            task_id = cur.fetchone()[0]
            conn.commit()
            return jsonify({'message': 'Task created', 'task_id': task_id}), 201
    finally:
        conn.close()

@app.route('/tasks/<int:task_id>', methods=['PUT'])
@token_required
def update_task(user_id, task_id):
    data = request.get_json()
    if not data:
        return jsonify({'message': 'No data provided'}), 400

    fields = []
    values = []

    for key in ('name', 'category', 'is_selected', 'is_favorite'):
        if key in data:
            fields.append(f"{key} = %s")
            values.append(data[key])

    if not fields:
        return jsonify({'message': 'No valid fields to update'}), 400

    values.extend([task_id, user_id])

    conn = get_db_connection()
    if conn is None:
        return jsonify({'message': 'Database connection error'}), 500
    try:
        with conn.cursor() as cur:
            cur.execute(f"""
                UPDATE tasks SET {', '.join(fields)}
                WHERE id = %s AND user_id = %s RETURNING id
            """, tuple(values))
            if cur.fetchone():
                conn.commit()
                return jsonify({'message': 'Task updated', 'task_id': task_id}), 200
            else:
                return jsonify({'message': 'Task not found'}), 404
    finally:
        conn.close()

@app.route('/tasks/<int:task_id>', methods=['DELETE'])
@token_required
def delete_task(user_id, task_id):
    conn = get_db_connection()
    if conn is None:
        return jsonify({'message': 'Database connection error'}), 500
    try:
        with conn.cursor() as cur:
            cur.execute("DELETE FROM tasks WHERE id = %s AND user_id = %s RETURNING id", (task_id, user_id))
            if cur.fetchone():
                conn.commit()
                return jsonify({'message': 'Task deleted', 'task_id': task_id}), 200
            else:
                return jsonify({'message': 'Task not found'}), 404
    finally:
        conn.close()

if __name__ == '__main__':
    from waitress import serve
    logging.info("Servidor Flask corriendo en http://0.0.0.0:5000")
    serve(app, host='0.0.0.0', port=5000)
