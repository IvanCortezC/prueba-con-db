# Enunciado evaluación parcial 2

## Programación Orientada a Objetos

| Sigla | Asignatura | Experiencia de Aprendizaje |
| --- | --- | --- |
| DSY1102 | Programación Orientada a Objetos | EA2: Desarrollo de Interfaces Gráficas con Persistencia en Archivo |

| Tiempo | Modalidad de Trabajo | Indicadores de logro |
| --- | --- | --- |
| 3 bloques | Individual | IL 2.1 al IL 2.8 |

---

## Condiciones de la evaluación

### Del entorno y la entrega

* La evaluación es de carácter individual. No está permitida ninguna forma de colaboración entre estudiantes.
* No se permite el acceso a internet ni el uso de teléfonos, relojes inteligentes ni ningún otro dispositivo externo.
* El programa debe compilar y ejecutarse correctamente. Una solución que no compile no permite evidenciar ningún indicador de logro.
* El proyecto debe entregarse comprimido en formato ZIP, con tu nombre completo como nombre del archivo, en la actividad habilitada en AVA antes de que finalice la sesión.
* No se aceptan entregas por ningún otro medio ni fuera del horario establecido. La ausencia de entrega en AVA genera automáticamente la nota mínima.

### De tu solución

* El código debe organizarse en clases separadas, respetando las convenciones de nomenclatura de Java: PascalCase para nombres de clase, camelCase para métodos y atributos.
* Puedes crear métodos o clases auxiliares siempre que no contradigan los requerimientos del caso.
* No se admite el uso de herramientas de inteligencia artificial ni de recursos digitales externos.
* Cualquier indicio de colaboración no autorizada, uso de IA o plagio implica la anulación inmediata de la evaluación con nota mínima de 1.0.

### Instrucciones de entrega

* Comprime el proyecto completo (todas las clases) en un único archivo ZIP.
* Nombra el archivo con tu nombre completo, sin espacios ni tildes (ejemplo: `Juan_Perez_Lopez.zip`).
* Sube el archivo a la actividad de entrega habilitada en AVA antes de que finalice la sesión asignada.
* No se aceptan entregas por correo electrónico, USB ni ningún otro medio.
* La ausencia de entrega en AVA genera automáticamente la nota mínima, independientemente del avance de la solución.

---

## 1. Contexto del caso

El centro de adopción animal **Huella Segura** ya cuenta con el modelo de dominio desarrollado en la evaluación anterior: el sistema trabaja con la interfaz **`Animal`** y las clases **`Perro`** y **`Gato`**, calcula el aporte base o cuota de adopción sugerida según el tipo de animal y permite registrar o consultar características propias de cada especie (raza, vacunas y aptitud para niños en los perros; nivel de sociabilidad en los gatos). Sin embargo, ese sistema solo funciona por consola y la información se pierde cada vez que el programa se cierra.

La coordinación del refugio ha solicitado transformarlo en una aplicación de escritorio de una sola ventana, que permita al equipo de recepción registrar y dar de baja animales, y que conserve la información en archivos para que esté disponible la próxima vez que se abra la aplicación.

### Proyecto base

Al inicio de la evaluación, el/la docente te entregará un proyecto Maven con la configuración inicial, la clase de arranque y el modelo ya implementado y probado (interfaz `Animal` y clases `Perro` y `Gato`). Tu trabajo consiste en construir sobre ese modelo la interfaz gráfica y el almacenamiento en archivos. **No debes reescribir ni reemplazar las clases entregadas.**

El modelo entregado ya aplica las siguientes reglas de cuota de adopción estimada:

* **Perro**: cuota base de **$25.000**. Si el perro cuenta con **vacunas al día**, la cuota se incrementa en un **20%**.
* **Gato**: cuota base de **$18.000**. Si el gato tiene un nivel de **sociabilidad de 4,0 o más** (perfil *indoor*), la cuota se incrementa en un **15%**. El perfil *indoor* no se registra como campo separado: se deduce de la sociabilidad, que es el único dato específico del gato en el formulario.

**Datos comunes**: el nombre no puede ser nulo ni vacío, el año de ingreso o nacimiento estimado debe estar entre **2010** y **2026** y la **edad en años** debe ser un valor entero mayor que **cero**. Si un dato no cumple, el modelo lo rechaza con un mensaje que explica el motivo.

---

## 2. Estructura del proyecto

La solución debe separar responsabilidades según el patrón **Modelo-Vista-Controlador**, con una capa de acceso a datos basada en el patrón **DAO/Repository**. El proyecto debe contener, como mínimo:

* **Configuración del proyecto**: el proyecto base ya incluye las dependencias de JavaFX, la clase de arranque de la aplicación y la dependencia de **Gson** en el `pom.xml`. El JAR de Gson se entrega en `libs.zip`, junto con el resto de las dependencias, para que puedas trabajar sin conexión. Consulta `docs/INSTRUCCIONES.md` para instalarlas.
* Distribución de paquetes y responsabilidades acorde al patrón de diseño (`model` con `Animal`, `Perro`, `Gato`, `view`, `controller`, `repository` o `dao`).

---

## 3. Ventana de la aplicación

Al ejecutar la aplicación se abre una única ventana, desde la cual se realizan todas las acciones. La ventana debe contener:

* Un **formulario de registro** con los campos descritos más abajo y un botón **Registrar**.
* Una **tabla** con los animales registrados y las columnas **Especie, Nombre, Año, Edad (años) y Cuota estimada**. El valor que se muestra es el que calcula el modelo (`Animal`) para cada registro.
* Un botón **Eliminar**, que quita del registro al animal seleccionado en la tabla. Si no hay una fila seleccionada, la aplicación informa que se debe seleccionar un animal y no realiza ningún cambio.
* Un **área de mensajes** dentro de la misma ventana, donde se informa el resultado de cada acción: registro exitoso, dato no válido, eliminación, falta de selección o error al leer o guardar los archivos. Cada mensaje reemplaza al anterior. **Los mensajes no se muestran por consola.**

### Campos del formulario

* Un selector de tipo con las opciones **Perro** y **Gato**.
* Datos comunes: **nombre**, **año de registro/nacimiento** y **edad (años)**.
* Datos de **Perro**: **raza**, si tiene **vacunas al día** (sí/no) y si es **apto para niños** (sí/no).
* Datos de **Gato**: **nivel de sociabilidad** (escala numérica con decimales, por ejemplo de `1,0` a `5,0`).

Al cambiar el tipo en el selector, solo quedan habilitados los datos específicos del tipo elegido; los datos del otro tipo quedan deshabilitados y no se consideran al registrar.

Al presionar **Registrar**, la aplicación valida los datos según la sección 4. Si son correctos, registra al animal, la tabla lo muestra de inmediato, el área de mensajes informa la confirmación (por ejemplo: *"Rocky (Perro) registrado correctamente."*) y el formulario queda vacío para un nuevo registro.

---

## 4. Validaciones del formulario

Antes de registrar, el formulario debe verificar los datos ingresados según las siguientes reglas:

* **Tipo (Perro / Gato)**: debe estar seleccionado.
* **Nombre**: no puede estar vacío ni contener solo espacios.
* **Año**: número entero entre **2010** y **2026**.
* **Edad (años)**: número entero mayor que **cero**.
* **Raza** (solo Perro): no puede estar vacía ni contener solo espacios.
* **Nivel de sociabilidad** (solo Gato): número mayor que **cero**; acepta decimales.

Las reglas de nombre, año y edad son las mismas que ya aplica el modelo entregado. Si algún dato no es válido:

* el animal **no se registra**;
* el área de mensajes indica qué campo es incorrecto y por qué;
* los valores ya ingresados se mantienen para que puedan corregirse;
* la aplicación no se detiene ni muestra errores por consola.

---

## 5. Almacenamiento de la información

La información de los animales debe conservarse entre una ejecución y otra, en formato **JSON**, en la carpeta raíz del proyecto:

* `perros.json`: contiene los perros registrados.
* `gatos.json`: contiene los gatos registrados.

El comportamiento esperado es el siguiente:

* Al iniciar, la aplicación carga automáticamente la información de ambos archivos y la muestra en la tabla.
* Si los archivos no existen (por ejemplo, la primera vez que se ejecuta), la aplicación inicia con la tabla vacía y sin mostrar errores.
* Cada vez que se registra o se elimina un animal, los archivos se actualizan automáticamente, sin que el usuario deba presionar un botón para guardar.
* Lo que muestra la tabla y lo guardado en los archivos debe coincidir en todo momento.

---

## 6. Datos de prueba y resultado esperado

Registra desde la aplicación los siguientes animales:

| Especie | Nombre | Año | Edad (años) | Datos específicos | Cuota estimada |
| --- | --- | --- | --- | --- | --- |
| Perro | Rocky | 2022 | 4 | Raza: Mestizo · Vacunas al día: sí · Apto niños: sí | $30.000 |
| Perro | Toby | 2024 | 2 | Raza: Beagle · Vacunas al día: no · Apto niños: no | $25.000 |
| Gato | Luna | 2023 | 3 | Sociabilidad: 4,5 pts | $20.700 |
| Gato | Mishi | 2025 | 1 | Sociabilidad: 2,0 pts | $18.000 |

Luego de registrarlos, la ventana debe verse de forma similar a la siguiente:

> *Figura 1. Ventana con los cuatro animales registrados.* `[pendiente: capturar pantalla real de la app]`

Si se presiona **Eliminar** sin seleccionar una fila, el aviso aparece en la misma ventana:

> *Figura 2. Aviso por falta de selección.* `[pendiente: capturar pantalla real de la app]`

Si un dato del formulario no es válido, el área de mensajes indica el campo con problemas y se mantienen los valores ingresados:

> *Figura 3. Error de validación en el año.* `[pendiente: capturar pantalla real de la app]`

### Verificación de almacenamiento

1. Cierra la aplicación y vuelve a abrirla: la tabla debe mostrar los mismos cuatro animales.
2. Elimina a Toby, cierra la aplicación y vuelve a abrirla: Toby ya no debe aparecer.

Las imágenes son referenciales: la distribución, los textos y el estilo de las ventanas pueden variar, siempre que estén presentes todos los elementos solicitados y que los valores calculados sean correctos.