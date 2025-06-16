# Proyecto Fin de Curso

**🏥Proyecto Hospital🩺**

El Hospital Carlos Haya nos ha encargado la gestión de su hospital, se trata de un trabajo de gran envergadura por ese motivo se plantea la realización del proyecto en grupo. El objetivo del sistema es digitalizar la administración de recursos humanos, infraestructura y pacientes, permitiendo una gestión eficiente del personal, salas, turnos y registros médicos.

El sistema debe permitir operar con diferentes perfiles de usuarios, médicos, enfermeros, administrativos, personal de mantenimiento y pacientes cada uno tendrá funcionalidades distintas. También deberá contemplar la gestión de las salas del hospital (habitaciones, quirófanos, consultorios), el historial médico de pacientes, los turnos del personal y otras funciones clave para el funcionamiento diario.

Algunas de las funciones de nuestro personal 👨‍⚕️🩺 (pero no las únicas) deben ser:

- Entre los empleados distinguiremos entre los administradores que podrá gestionar empleados y pacientes (registrar, eliminar o modificar), y podrá también gestionar salas. Los administrativos no administradores por su parte, podrán consultar todos los datos del hospital, asignar pacientes a médicos y dar citas.
- Los médicos podrán registrar un diagnóstico a un paciente y consultar su historial médico, registrar una receta, ver los pacientes que tiene asignados. Los enfermeros son los encargados de asignar cama a un paciente y marcar el alta médica de un paciente si el médico lo solicito.

El proyecto debe de contemplar los siguientes requerimientos:

- Gestión de Personal👑:
    - Alta, baja y modificación de empleados: médicos, enfermeros, administrativos, mantenimiento.
    - Asignación de roles, turnos y salas de trabajo.
    - Control de disponibilidad y horario.
- Gestión de Pacientes👩‍⚕️🩹:
    - Registro de pacientes (nombre, DNI, contacto, obra social, etc.).
    - Historial médico (consultas, diagnósticos, intervenciones).
    - Asignación de habitaciones.
    - Solicitudes de atención médica.
- Gestión de Infraestructura🏢:
    - Registro de salas del hospital (habitaciones, quirófanos, consultorios).
    - Control de disponibilidad y ocupación.
    - Mantenimiento de salas.
- Turnos y Consultas🧑‍⚕️ :
    - Solicitud y asignación de turnos.
    - Agenda de médicos y enfermeros.
    - Registro de visitas, diagnósticos y tratamientos.
- Reportes y Estadísticas 👷‍♂️🔧:
    - Listado de pacientes internados actualmente.
    - Disponibilidad de habitaciones.
    - Reporte de actividad por médico o enfermero.
    - Historial clínico completo de un paciente.

El proyecto debe tener **persistencia**, es decir debe mantener los datos al finalizar el proyecto y recuperar al volver a iniciar el programa. Podrá guardarse en ficheros o en base de datos, quedará a elección del grupo.

Presentará una **interfaz gráfica** básica para registrar los datos de los diferentes recursos. Aunque los listados pueden realizarse por consola.

Los datos deberán contar con **validaciones** y controlarse para que no ocurran errores.

Se deberá diseñar un **manual de uso** del sistema, el objetivo es que cualquier persona sin conocimientos técnicos pueda entender como utilizar el sistema, cuales son sus funiones y como se realizan las principales acciones desde la interfaz de usuario.

**Evaluación:**

Se deberán mantener dos reuniones previas a la presentación del proyecto:

**1 Reunión:**

- Presentación de un diagrama de clases y casos de uso del proyecto.
- Reparto de tareas y roles entre los integrantes, los roles más normales dentro de un grupo de desarrollo son:
    - Desarrollador Back-End encargado de la lógica y modelo de Datos, será el encargado de diseñar e implementar las clases principales, también implementará las validaciones y gestionará la relación entre objetos y comportamientos.
    - Desarrollador Front-End será el encargado de implementar la interfaz gráfica y conectarla con la lógica del sistema. Diseñará los formularios, menús, listados....
    - Responsable de la Persistencia de Datos, será el encargado de diseñar como se van a guardar y recuperar los datos e implementará la carga y guardado automático. Debe velar para que no haya datos corruptos o duplicados.
    - Coordinador del Proyecto / Testeador / Documentador su función es coordinar la comunicación entre los miembros y planificar las tareas para asegurarse de que todo esté avanzado, además probará el sistema y documentará el código.
- Primer boceto de la interfaz gráfica y primeras clases implementadas.

**2 Reunión :**

- Demo parcial del sistema.
- Que partes están finalizadas y cuales están en curso.
- Que partes se plantean como mejoras para un futuro.
- Como tienen previsto desarrollar la demo final

**Presentación final**

Cada grupo debe preparar una presentación de entre 30 y 40 minutos donde expongan:

- Las funcionalidades implementadas
- La estructura del sistema (modelo y diseño)
- La lógica detrás de su arquitectura
- Y una demostración en directo de las principales funciones del sistema.
