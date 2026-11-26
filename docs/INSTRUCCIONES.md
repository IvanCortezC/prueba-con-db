# Instrucciones de dependencias (equipos sin internet)

El `pom.xml` ya declara JavaFX, ControlsFX y Gson. **No hay que agregar Gson ni ninguna otra dependencia al `pom.xml`**: solo hay que hacer que los JAR estén disponibles en el repositorio local de Maven del equipo. Para eso se genera un `libs.zip` y se lo entrega a los estudiantes.

---

## Para el docente: generar `libs.zip` en Windows

1. Con internet, abre PowerShell o CMD en la carpeta raíz del proyecto (donde está `pom.xml`).

2. Descarga exclusivamente lo que usa el proyecto en un repositorio temporal **dentro de la carpeta raíz del proyecto**. Ejecuta los comandos desde esa carpeta y usa la ruta absoluta para no tener dudas:

   - En **CMD**:

     ```
     mvn "-Dmaven.repo.local=%CD%\repo-temp" dependency:go-offline
     mvn "-Dmaven.repo.local=%CD%\repo-temp" clean compile javafx:run
     ```

   - En **PowerShell**:

     ```
     mvn "-Dmaven.repo.local=$PWD\repo-temp" dependency:go-offline
     mvn "-Dmaven.repo.local=$PWD\repo-temp" clean compile javafx:run
     ```

   - El primer comando baja las dependencias y los plugins declarados.
   - El segundo fuerza la descarga de los plugins del ciclo de vida y de los JAR **nativos de JavaFX para Windows**. Cuando se abra la ventana, ciérrala para que el comando termine.
   - Hazlo en Windows: así los natives incluidos son `win-x64`, que es lo que necesitan los estudiantes.
   - Al terminar, el directorio queda en `<carpeta del proyecto>\repo-temp`. La forma corta `-Dmaven.repo.local=repo-temp` también lo crea ahí, siempre que ejecutes el comando en la raíz del proyecto.

3. Revisa que dentro de `<carpeta del proyecto>\repo-temp` estén, como mínimo:

   - `org\openjfx\` (javafx-base, javafx-controls, javafx-fxml, javafx-graphics y el JAR con clasificador `win-x64`),
   - `org\controlsfx\`,
   - `com\google\code\gson\`,
   - `org\apache\maven\plugins\` y `org\openjfx\javafx-maven-plugin\`.

4. Crea una carpeta llamada `repository`, copia **todo** el contenido de `<carpeta del proyecto>\repo-temp` dentro y comprime esa carpeta como `libs.zip`. Dentro del ZIP debe aparecer `repository\...` como carpeta raíz.

5. Entrega `libs.zip` a los estudiantes por el medio que uses normalmente (por ejemplo, adjunto en la actividad de AVA). Después puedes borrar `repo-temp`: no debe viajar en la entrega, porque `libs.zip` ya contiene lo necesario.

---

## Para los estudiantes: usar el `libs.zip` que entrega el docente

1. Copia el proyecto a tu computador y guarda el `libs.zip` recibido del docente. **No necesitas internet.**

2. Extrae `libs.zip` en tu carpeta de usuario de Windows:

   - Abre `libs.zip` y arrastra la carpeta `repository` hasta `%USERPROFILE%\.m2\` (por ejemplo, `C:\Users\TuUsuario\.m2\`). Si la carpeta `.m2` no existe, créala.
   - El resultado debe ser `C:\Users\TuUsuario\.m2\repository\org\openjfx\...`, `...\com\google\code\gson\...`, etc.
   - Si IntelliJ usa otro repositorio local, revisa *File > Settings > Build, Execution, Deployment > Build Tools > Maven > Local repository* y extrae ahí la carpeta `repository`.

3. Abre el proyecto en IntelliJ IDEA:

   - *File > Open...* y selecciona la carpeta del proyecto.
   - Acepta *Trust Project* y ábrelo como proyecto Maven.
   - En *File > Project Structure > Project* verifica que el JDK sea **21**, que es el que indica el `pom.xml` (`<release>21</release>`). Si IntelliJ propone otro SDK, selecciona el JDK 21 instalado en el equipo.

4. Recarga Maven con el ícono *Load Maven Changes* (o clic derecho en `pom.xml > Maven > Reload Project`). Si aparecen dependencias sin resolver, activa el modo offline de Maven (*Toggle Offline Mode*) y ejecuta en la terminal:

   ```
   mvn -o clean compile
   ```

5. Ejecuta la aplicación:

   - botón *Run* sobre `com.duoc.adoptame.Launcher`, o
   - `mvn -o javafx:run`.

   Debe abrirse la ventana del centro de adopción **Huella Segura**.

6. Si Maven sigue intentando descargar algo, verifica que la carpeta `repository` quedó directamente dentro de `.m2` y no dentro de `libs.zip\repository\repository`. No modifiques el `pom.xml`: Gson ya está declarado ahí y su JAR viene en `libs.zip`.
