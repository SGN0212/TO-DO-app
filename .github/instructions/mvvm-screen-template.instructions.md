---
applyTo: "**/*.kt"
---

# Template para solicitudes de nuevas pantallas usando MVVM (sin LiveData, con listas comunes o mutables)

When I @request-new-screen {screenName}, follow this pattern:

1. Crea el layout XML con **ViewBinding** habilitado (`buildFeatures.viewBinding = true` en `build.gradle`).
2. Genera un **Fragment** (o Activity) llamado `{screenName}Fragment` con el layout inflado usando ViewBinding.
3. Crea un **ViewModel** llamado `{screenName}ViewModel` y usa variables comunes o listas mutables para manejar el estado de UI:
   - `val items = mutableListOf<Task>()`
   - `var selectedCategory: String = ""`
4. La UI debe obtener datos directamente desde el ViewModel sin LiveData ni StateFlow.
5. Si necesitas persistencia o lógica compleja, agrega un repositorio separado (`{screenName}Repository`) y úsalo desde el ViewModel.
6. Usa **Navigation Component** para navegación si está habilitado.
7. Separa responsabilidades: la UI solo muestra datos y responde a eventos; el ViewModel contiene la lógica de negocio.

Opcional:
- Para pasar datos entre pantallas, usa **SafeArgs** o un `Bundle` simple.
- Si usas `notifyDataSetChanged()`, actualiza correctamente las listas en el ViewModel.