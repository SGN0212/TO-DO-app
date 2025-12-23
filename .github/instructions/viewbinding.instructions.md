---
applyTo: "ui/**/*.kt"
---

# Especificaciones para uso de ViewBinding en layouts Android

- Always bind your views using ViewBinding: inflate binding in `onCreateView()` or `onCreate()`.
- Do not use `findViewById`.
- Use `binding.apply { ... }` or `with(binding)` when interacting with multiple views.
- Set `binding = null` in `onDestroyView()` in Fragments.