# Generación asistida de arquitectura hexagonal

La IA propuso:

- Dominio: entidades + objetos de valor.
- Application: casos de uso y puertos.
- Infrastructure: adaptadores (SOAP mock + repositorio).
- Web: controladores generados desde OpenAPI.

Esta estructura fue adoptada casi por completo, con ajustes en:
- Paquetes internos.
- Implementación estricta de interfaces generadas.
- Integración del simulador SOAP dentro de Infrastructure.
