# Configuración en Render

## Variables de Entorno Requeridas

En el dashboard de Render, en la sección **Environment Variables**, debes agregar:

```
PASS=<tu_contraseña_postgresql>
PORT=10000
```

Reemplaza `<tu_contraseña_postgresql>` con la contraseña real de tu BD PostgreSQL.

## Verificación de Conexión

1. La URL de la BD en `application.properties` es:
   ```
   jdbc:postgresql://ep-purple-sea-a4sykv86-pooler.us-east-1.aws.neon.tech:5432/neondb?sslmode=require
   ```

2. Usuario: `neondb_owner`

3. La BD debe estar creada y accesible desde Render

## Logs en Render

Si sigues recibiendo error, habilita debug:
- En Render, ve a Settings → Build & Deploy
- Agrega variable: `JAVA_OPTS=-Ddebug=true`

## Build Command

Render debería estar ejecutando:
```bash
mvn clean package -DskipTests
```

Si no es así, asegúrate en Render:
- Settings → Build & Deploy → Build Command:
  ```
  mvn clean package -DskipTests
  ```

## Troubleshooting

**Error: "org.postgresql.util.PSQLException"**
- La contraseña PASS está mal configurada
- La BD no existe
- No hay conexión a la BD desde Render

**Error: "ApplicationContext failed to start"**
- Revisa en Render los logs completos (Live Log tab)
- Asegúrate que todas las variables de entorno están definidas
