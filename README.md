# HospitalManagmentSystem
Sistema de Gestión Hospitalaria "Carlos Haya"
Este proyecto presenta el Sistema de Gestión Hospitalaria "Carlos Haya", una aplicación completa desarrollada en Java. Su propósito principal es digitalizar y centralizar la administración de los recursos humanos, la infraestructura y los pacientes dentro de un hospital. El sistema ha sido diseñado para optimizar la gestión del personal médico y administrativo, las diferentes salas disponibles (como habitaciones, quirófanos y consultorios), los turnos de trabajo y los historiales médicos de los pacientes.

Una de las características más importantes es la diferenciación de perfiles de usuario, incluyendo médicos, enfermeros, administrativos, personal de mantenimiento y pacientes. Cada uno de estos roles tiene accesos y funcionalidades específicas, lo que garantiza una operación segura, organizada y adaptada a las necesidades reales del entorno hospitalario. La aplicación cuenta con una interfaz gráfica sencilla que facilita la carga y consulta de datos. Además, asegura que la información se guarde de forma permanente utilizando una base de datos MySQL, e incluye validaciones para mantener la integridad de los datos.

🚀 Tecnologías Utilizadas
Para el desarrollo de este sistema, se emplearon diversas herramientas y tecnologías:

Lenguaje de Programación: Java
Interfaz Gráfica: Java Swing
Entornos de Desarrollo (IDEs): Se utilizaron Eclipse y Visual Studio Code.
Control de Versiones: Git y GitHub fueron esenciales para el control de versiones y el trabajo colaborativo en equipo.
Base de Datos: MySQL, gestionada a través de XAMPP y phpMyAdmin.
Conectividad con la Base de Datos: JDBC (Java Database Connectivity) se usó para establecer la conexión entre Java y MySQL.
📂 Estructura del Proyecto
El proyecto está organizado en tres componentes principales: el Front-End (la interfaz de usuario), el Back-End (la lógica del negocio) y la Base de Datos (para el almacenamiento de la información).

Front-End
La interfaz de usuario se diseñó pensando en ser atractiva y funcional, buscando optimizar tanto la estética como la experiencia del usuario:

Marco Principal: Un elemento principal que contiene un panel de imagen fijo. Este panel actúa como un fondo visual constante para toda la aplicación.
Panel de Inicio de Sesión: Es el primer punto de interacción, donde los usuarios ingresan sus credenciales para acceder al sistema.
Navegación Fluida: Los diferentes paneles de contenido se superponen al fondo fijo, creando una sensación de "cambio de pantalla" sin alterar la base visual, lo que resulta en una experiencia más suave.
Diseño Coherente: Se eligió una paleta de colores predominante con tonos de azul para los fondos de los paneles y letras blancas para asegurar una excelente legibilidad.
Estructura Modular: Los paneles operativos se dividen en tres áreas claras:
Cabecera: Un panel superior que muestra el título de la sección, ayudando al usuario a saber dónde se encuentra dentro de la aplicación.
Cuerpo Principal: Esta es la parte más dinámica y se subdivide en:
Panel de Funciones (Izquierda): Contiene botones que representan diferentes funciones o categorías de información. Al hacer clic en ellos, se activa la visualización de datos.
Panel de Visualización (Derecha): Aquí es donde se muestra la información detallada, como tablas de datos o formularios, que se activan al interactuar con el panel de funciones.
Carga Automática de Datos: Para una experiencia eficiente, la información relevante se carga y se muestra automáticamente al abrir un panel, eliminando la necesidad de acciones adicionales por parte del usuario para ver los datos iniciales.
Back-End
El diseño de la lógica del sistema se basa en un enfoque orientado a objetos, utilizando clases bien definidas para representar cada elemento del hospital:

Clase Principal Hospital: Actúa como el núcleo del sistema, encargándose de almacenar y gestionar las listas de empleados, pacientes, salas y citas.
Clase Base Usuario: Contiene atributos comunes a todos los usuarios, como nombre, apellido, DNI y rol. Las clases Empleado y Paciente heredan de esta.
Clase Empleado y Sus Roles Específicos: Representa a cualquier trabajador del hospital. De ella se derivan roles como Administrador, Médico, Enfermero, Administrativo y Personal de Mantenimiento. Cada rol tiene métodos que reflejan sus tareas específicas.
Clase Paciente: Hereda de Usuario y tiene atributos propios relacionados con la atención médica, como la obra social, el historial clínico y el estado de internación. Cada paciente posee un objeto HistorialMedico para almacenar diagnósticos y tratamientos.
Clase Cita: Representa una cita programada entre un paciente y un médico, ayudando a organizar el calendario de atención.
Clase Sala y Sus Tipos: Es una clase general para cualquier espacio físico del hospital. Se subdivide en Habitación, Quirófano y Consultorio, cada una con características específicas.
Persistencia de Datos: La clase DBConnection se encarga de conectar la aplicación Java con la base de datos MySQL (usando JDBC) para guardar, recuperar y actualizar los datos de forma automática al iniciar o cerrar el programa.
Base de Datos
Se optó por MySQL para el almacenamiento de los datos, lo que permite una gestión confiable y organizada. La estructura de la base de datos se basa en las clases del back-end, creando tablas para cada entidad (como diagnostico, paciente, empleado). Se establecen claves primarias para identificar de forma única los registros y claves foráneas para crear relaciones entre las tablas, asegurando la coherencia y la integridad de la información.

Además de la estructura, se incluyeron registros iniciales en la base de datos para facilitar las pruebas del funcionamiento tanto del front-end como del back-end.

🧪 Pruebas Realizadas
Durante el desarrollo del sistema, se llevaron a cabo pruebas manuales para asegurar que todas las funcionalidades principales operaran correctamente. Estas pruebas se enfocaron en verificar el buen funcionamiento de las acciones clave del programa, así como la validez de los datos y los accesos según los perfiles de usuario.

Algunas de las pruebas realizadas incluyeron:

Gestión de Personal: Se verificó la creación, modificación y eliminación de empleados (médicos, enfermeros, administrativos, etc.), asegurando que los datos se guardaran correctamente y que no se permitiera el ingreso de información inválida.
Gestión de Pacientes: Se comprobó el registro y la asignación de pacientes, la visualización de historiales médicos, la asignación de camas y las altas médicas.
Inicio de Sesión y Control de Acceso: Se confirmó que los usuarios pudieran iniciar sesión correctamente con sus credenciales y que fueran dirigidos a la sección adecuada según su rol.
Interacción con la Base de Datos: Se validó que los datos se almacenaran y recuperaran correctamente desde MySQL, garantizando que la información persistiera entre las ejecuciones del programa.
Resultados:
Las pruebas demostraron que el sistema responde adecuadamente a los distintos escenarios planteados y que se respetan las validaciones implementadas para mantener la integridad de los datos y los permisos de acceso para cada perfil de usuario.

✨ Funcionalidades del Proyecto
El sistema de gestión hospitalaria permite una serie de acciones clave orientadas a la administración eficiente del hospital, organizadas por áreas y tipos de usuarios:

Gestión de Personal
Registro, modificación y eliminación de empleados (médicos, enfermeros, administrativos, mantenimiento).
Asignación de roles y turnos de trabajo.
Control de disponibilidad y horarios del personal.
Acceso diferenciado a las funcionalidades según el rol del usuario.
Gestión de Pacientes
Registro de nuevos pacientes con sus datos personales y de contacto.
Asignación de médicos y habitaciones a los pacientes.
Registro y consulta del historial clínico completo.
Gestión de solicitudes médicas.
Gestión de Infraestructura
Registro y modificación de las diferentes salas del hospital (habitaciones, quirófanos, consultorios).
Consulta de la disponibilidad y ocupación de las salas.
Registro de salas que se encuentran en mantenimiento.
Turnos y Consultas
Asignación de turnos a pacientes con médicos específicos.
Consulta y gestión de la agenda de médicos y enfermeros.
Registro de visitas, diagnósticos y recetas médicas.
Inicio de Sesión y Control de Acceso
Acceso al sistema mediante el uso de usuario y contraseña.
Restricción de funcionalidades según el tipo de usuario (su rol).
Validaciones implementadas para evitar accesos no autorizados.
Reportes y Listados
Generación de listados de pacientes actualmente internados.
Informes sobre la disponibilidad de camas y salas.
Reportes de actividad por profesional médico o enfermero.
Consulta del historial completo de un paciente específico.
Persistencia de Datos
Almacenamiento de toda la información en una base de datos MySQL.
Conexión segura entre la aplicación Java y la base de datos.
Recuperación automática de los datos al iniciar el programa.
📸 Capturas de Pantalla
(Aquí se insertarían las capturas de pantalla del proyecto, mostrando la interfaz de usuario y las diferentes funcionalidades.)

💡 Consideraciones Finales
El sistema actual cumple satisfactoriamente con los requisitos principales del proyecto, permitiendo una gestión digital y organizada del personal, los pacientes y la infraestructura del hospital. Sin embargo, existen varias funcionalidades y mejoras que podrían implementarse en futuras versiones para optimizar aún más el sistema:

Mejoras Funcionales
Implementación de roles con autenticación real y un control de acceso más seguro, incluyendo el cifrado de contraseñas.
Desarrollo de un sistema de gestión de stock de medicamentos y material médico, con alertas para bajos niveles de existencias.
Creación de un sistema de notificaciones internas entre el personal del hospital (por ejemplo, avisos entre médicos y enfermeros).
Generación automática de citas, basándose en la disponibilidad de médicos y pacientes.
Mejoras Técnicas
Migración a JavaFX para una interfaz gráfica más moderna, flexible y amigable para el usuario.
Optimización de las conexiones a la base de datos para mejorar el rendimiento cuando se manejan grandes volúmenes de datos.
Implementación de validaciones más robustas, con retroalimentación visual directa en la interfaz para el usuario.
Desarrollo de la internacionalización del sistema para permitir su uso en varios idiomas.
