---
applyTo: "**/*.kt"
---

# Guía central para la arquitectura Android usando MVVM, ViewBinding y estilo Kotlin limpio

- Usa la arquitectura **MVVM**: separa claramente **Data** (repositorios), **Domain** (use cases o ViewModel) y **UI** (Activities/Fragments).
- Todas las vistas deben usar **ViewBinding** (no Kotlin synthetic) para acceder a componentes de UI.
- El **ViewModel** debe exponer estados usando **LiveData** o **StateFlow** inmutable (ej. `val uiState: StateFlow<...>`).
- Los repositorios deben ser **interfaces inyectadas** (preferiblemente con **Hilt** o **Koin**).
- Las clases en `ui/` solo deben contener lógica de UI, **no lógica de negocio**.
- Añade comentarios Kotlin en **español** explicando propósitos de funciones. Evita comentarios reiterativos.
- Sigue convención Kotlin: `PascalCase` para clases, `camelCase` para funciones, evita usar `any`, declara tipos de retorno explícitos.