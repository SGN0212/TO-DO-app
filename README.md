Este proyecto es una solución integral de gestión de tareas que demuestra la sincronización
entre un cliente móvil nativo y un ecosistema backend en la nube. A diferencia de una aplicación local,
Task Master implementa persistencia remota, manejo de estados y una arquitectura escalablediseñada
bajo principios de ingeniería de software.

Características Principales
Arquitectura MVVM: Separación clara de responsabilidades para un código mantenible.

Optimistic UI: Actualización inmediata de la interfaz local con lógica de rollback ante fallos de red para una experiencia de usuario fluida.

Full Stack Integration: API REST propia desplegada en Render y base de datos relacional en Supabase.

Gestión de Sesión y Datos: Manejo de variables de entorno y seguridad en la comunicación cliente-servidor.

Stack Tecnológico
Mobile: Kotlin, Android SDK, ViewBinding, Fragments.

Backend: Python, Flask (API REST).

Database: PostgreSQL (Supabase).

DevOps: Render (Hosting API), Git para control de versiones.

Arquitectura y Diseño
El proyecto fue migrado de una estructura basada en Activities a una de Fragments,
optimizando el ciclo de vida de la aplicación. Se implementó una lógica de negocio
donde la interfaz de usuario no depende directamente de la respuesta del servidor 
para mostrar cambios, mejorando la percepción de velocidad (UX).
