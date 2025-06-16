package clases;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class DBConnection {

	private static final String DB_URL = "jdbc:mysql://localhost:3306/appcarloshaya";
	private static final String DB_USUARIO = "root"; // CREO QUE ES ROOT POR DEFECTO PERO LO QUE PONDRA JAVI
	private static final String DB_CONTRASENIA = ""; // CONTRASEÑA DE BASE DATOS

	private static Connection conn = null; // PARA TENER UNA UNICA CONEXION (MODO CONTROL)

	public DBConnection() {
		// SE HACE UN CONSTRUCTOR VACIO PARA EVITAR LA ISTANCIA DIRECTA
	}

	public void conectar() {
		if (conn == null) {
			try {
				conn = DriverManager.getConnection(DB_URL, DB_USUARIO, DB_CONTRASENIA);
				System.out.println("Conexión a app Carlos haya establecida.");
			} catch (SQLException e) {
				System.out.println("Error al conectar a la base de datos");
			}
		}
	}

	public void desconectar() {
		conn = null;
	}

	// ########################### CLASE USUARIO ###########################

	// -------------- GESTION DE LOGIN --------------

	public boolean comprobarPrimerLogin(String dni, String nuevaContrasena) {
	
		Statement declaracionSQL = null; // Declaración de la sentencia
		ResultSet conjuntoResultados = null; // Declaración del resultado de la consulta

		try {
			conectar();
			declaracionSQL = conn.createStatement();
			String sqlVerificacion = "SELECT contrasena FROM Empleado WHERE usuario_dni = '" + dni + "'";
			conjuntoResultados = declaracionSQL.executeQuery(sqlVerificacion);
			if (conjuntoResultados.next()) {
				String contrasenaActual = conjuntoResultados.getString("contrasena");
				if (contrasenaActual.equalsIgnoreCase("null")) {
					
					String sqlActualizacion = "UPDATE Empleado SET contrasena = '" + nuevaContrasena + "' WHERE usuario_dni = '" + dni + "'";
					int filasAfectadas = declaracionSQL.executeUpdate(sqlActualizacion); 
					return filasAfectadas > 0;
				}
			} 
				return false;
		} catch (SQLException e) {
			e.printStackTrace(); 
			return false; 
		} finally {
			desconectar();
		}
	}

	public String iniciarSesion(String dni, String contrasena) {
		String sql = "SELECT u.rol FROM Usuario u " + "JOIN Empleado e ON u.dni = e.usuario_dni "
				+ "WHERE u.dni = ? AND e.contrasena = ?";
		try {
			conectar();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, dni);
			ps.setString(2, contrasena);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				return rs.getString("rol");
			} else {
				return null; // No coincide dni o contraseña
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			desconectar();
		}
	}

	public boolean registrar(Empleado empleado) {
		Statement stmt = null;
		try {
			conectar();
			stmt = conn.createStatement();

			String sqlUsuario = "INSERT INTO Usuario (nombre, apellido, dni, rol) VALUES ('" + empleado.getNombre()
					+ "', '" + empleado.getApellido() + "', '" + empleado.getDni() + "', '" + empleado.getRol() + "')";
			int filasUsuario = stmt.executeUpdate(sqlUsuario);

			if (filasUsuario == 0) {
				System.out.println("No se pudo insertar en la tabla Usuario.");
				return false;
			}

			String sqlEmpleado = "INSERT INTO Empleado (usuario_dni, contrasena) VALUES ('" + empleado.getDni() + "', '"
					+ empleado.getContrasena() + "')";

			int filasEmpleado = stmt.executeUpdate(sqlEmpleado);

			return filasEmpleado > 0;

		} catch (SQLException e) {
			System.out.println("Error al agregar empleado: " + e.getMessage());
			e.printStackTrace();
			return false;
		} finally {
			desconectar();
		}
	}

	// -------------- GESTION DE REGISTRAR --------------

	// ########################### CLASE ADMINISTRADOR ###########################

	// -------------- GESTION DE EMPLEADO --------------

	public boolean agregarEmpleado(Empleado empleado) {
		try {
			conectar();
			Statement stmt = conn.createStatement();

			// SE INSERTA PRIMERO EN TABLA DE USUARIO
			String sqlUsuario = "INSERT INTO Usuario (nombre, apellido, dni, rol) VALUES ('" + empleado.getNombre()
					+ "', '" + empleado.getApellido() + "', '" + empleado.getDni() + "', '" + empleado.getRol() + "')";
			int filasUsuario = stmt.executeUpdate(sqlUsuario);

			if (filasUsuario == 0) {
				return false;
			}
			// POSTERIORMENTE SE INGRESA EN LA TABLA EMPLEADO (USUARIO_ID ES EL DNI)
			String sqlEmpleado = "INSERT INTO Empleado (usuario_dni, contrasena, sala_id) VALUES ('" + empleado.getDni()
					+ "', '" + empleado.getContrasena() + "', '" + empleado.getSalaId() + "')";
			int filasEmpleado = stmt.executeUpdate(sqlEmpleado);

			return filasEmpleado > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			desconectar();
		}
	}

	public boolean modificarEmpleado(Empleado empleado) {
		try {
			conectar();
			Statement stmt = conn.createStatement();
			// SE MODIFICA SOLO LA TABLA USUARIO, YA QUE EL DNI ES LA ID Y NO ES
			// MODIFICABLE, Y SIEMPRE SERA DE TIPO EMPLEADO
			String sql = "UPDATE Usuario SET " + "nombre = '" + empleado.getNombre() + "', " + "apellido = '"
					+ empleado.getApellido() + "', " + "rol = '" + empleado.getRol() + "' " + "WHERE dni = '"
					+ empleado.getDni() + "'";

			int filas = stmt.executeUpdate(sql);
			return filas > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			desconectar();
		}
	}

	public boolean eliminarEmpleado(String dni) {
		try {
			conectar();
			Statement stmt = conn.createStatement();

			String sql = "DELETE FROM Usuario WHERE dni = '" + dni + "'";

			int filas = stmt.executeUpdate(sql);
			return filas > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			desconectar();
		}
	}

	public List<Empleado> obtenerTodosLosEmpleados() {
		List<Empleado> empleados = new ArrayList<>();
		conectar();

		String sql = "SELECT u.dni, u.nombre, u.apellido, u.rol, s.id AS sala_id, s.tipo AS sala_tipo "
				+ "FROM Usuario u JOIN Empleado e ON u.dni = e.usuario_dni LEFT JOIN Sala s ON e.sala_id = s.id "
				+ "WHERE u.rol IN ('administrativo', 'administrador', 'enfermero', 'mantenimiento', 'medico')";
		try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

			while (rs.next()) {
				String dni = rs.getString("dni");
				String nombre = rs.getString("nombre");
				String apellido = rs.getString("apellido");
				String rol = rs.getString("rol");
				String contrasena = null;
				int salaId = rs.getInt("sala_id");
				if (rs.wasNull()) {
					salaId = 0; // Por si aun no tiene asignado ninguna sala
				}
				empleados.add(new Empleado(salaId, nombre, apellido, dni, rol, contrasena));
			}
		} catch (SQLException e) {
			System.err.println("Error al obtener empleados: " + e.getMessage());
		} finally {
			desconectar();
		}
		return empleados;
	}

	public boolean asignarTurno(String empleadoDni, String pacienteDni, Date dia, Time horaInicio, Time horaFin) {
		conectar();
		if (conn == null)
			return false;

		String sql = "INSERT INTO turno (empleado_dni, paciente_dni, dia, hora_inicio, hora_fin) VALUES (?, ?, ?, ?, ?)";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, empleadoDni);
			pstmt.setString(2, pacienteDni);
			pstmt.setDate(3, dia);
			pstmt.setTime(4, horaInicio);
			pstmt.setTime(5, horaFin);

			int filas = pstmt.executeUpdate();
			if (filas > 0) {
				System.out.println("Turno asignado exitosamente.");
			} else {
				System.err.println("No se pudo asignar el turno.");
			}
			return filas > 0;
		} catch (SQLException e) {
			System.err.println("Error al asignar turno: " + e.getMessage());
			return false;
		} finally {
			desconectar();
		}
	}

	public boolean asignarSala(String empleadoDni, Integer salaId) {
		conectar();
		String salaIdValue;
		if (salaId == null || salaId == 0) {
			salaIdValue = "NULL";
		} else {
			salaIdValue = String.valueOf(salaId);
		}

		String sql = "UPDATE Empleado SET sala_id = " + salaIdValue + " WHERE usuario_dni = '" + empleadoDni + "'";

		try (Statement stmt = conn.createStatement()) {
			int filas = stmt.executeUpdate(sql);
			if (filas > 0) {
				System.out.println("Sala asignada/desasignada exitosamente.");
			} else {
				System.err.println("No se encontró el empleado para asignar/desasignar sala o no hubo cambios.");
			}
			return filas > 0;
		} catch (SQLException e) {
			System.err.println("Error al asignar sala: " + e.getMessage());
			return false;
		} finally {
			desconectar();
		}
	}

	// -------------- GESTION DE PACIENTE --------------

	public boolean agregarPaciente(Paciente paciente) {
		try {
			conectar();
			Statement stmt = conn.createStatement();
			String sqlUsuario = "INSERT INTO Usuario (nombre, apellido, dni, rol) VALUES ('" + paciente.getNombre()
					+ "', '" + paciente.getApellido() + "', '" + paciente.getDni() + "', 'paciente')";
			int filasUsuario = stmt.executeUpdate(sqlUsuario);

			if (filasUsuario == 0) {
				return false;
			}

			String sqlPaciente = "INSERT INTO Paciente (usuario_dni, contacto, obra_social, alta, sala_id) VALUES ('"
					+ paciente.getDni() + "', '" + paciente.getContacto() + "', '" + paciente.getObraSocial() + "', "
					+ (paciente.isAlta() ? 1 : 0) + ", " + paciente.getSalaID() + ")";
			int filasPaciente = stmt.executeUpdate(sqlPaciente);

			return filasPaciente > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			desconectar();
		}
	}

	public boolean modificarPaciente(Paciente paciente) {
		try {
			conectar();
			Statement stmt = conn.createStatement();

			// PRIMERO SE MODIFICA LA PARTE DE USUARIO
			String sqlUsuario = "UPDATE Usuario SET " + "nombre = '" + paciente.getNombre() + "', " + "apellido = '"
					+ paciente.getApellido() + "' " + "WHERE dni = '" + paciente.getDni() + "'";

			int filasUsuario = stmt.executeUpdate(sqlUsuario);

			// SEGUNDO SE MODIFICA LA PARTE DE PACIENTE
			String sqlPaciente = "UPDATE Paciente SET " + "contacto = '" + paciente.getContacto() + "', "
					+ "obra_social = '" + paciente.getObraSocial() + "', " + "alta = " + (paciente.isAlta() ? 1 : 0)
					+ ", " + "sala_id = " + paciente.getSalaID() + " WHERE usuario_dni = '" + paciente.getDni() + "'";

			int filasPaciente = stmt.executeUpdate(sqlPaciente);

			return (filasUsuario > 0 && filasPaciente > 0);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			desconectar();
		}
	}

	public boolean eliminarPaciente(String dni) {
		try {
			conectar();
			Statement stmt = conn.createStatement();
			// `ON DELETE CASCADE` asi que borra la otra tambien
			String sql = "DELETE FROM Usuario WHERE dni = '" + dni + "'";

			int filas = stmt.executeUpdate(sql);
			return filas > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			desconectar();
		}
	}

	public List<Paciente> obtenerTodosLosPacientes() {
		List<Paciente> pacientes = new ArrayList<>();
		conectar();
		String sql = "SELECT u.dni, u.nombre, u.apellido, p.contacto, p.obra_social, p.sala_id "
				+ "FROM Usuario u JOIN Paciente p ON u.dni = p.usuario_dni "
				+ "WHERE u.rol = 'paciente' AND p.alta = 0";
		try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

			while (rs.next()) {
				String dni = rs.getString("dni");
				String nombre = rs.getString("nombre");
				String apellido = rs.getString("apellido");
				String contacto = rs.getString("contacto");
				String obraSocial = rs.getString("obra_social");
				int salaId = rs.getInt("sala_id");
				if (rs.wasNull()) {
					salaId = 0;
				}

				pacientes.add(new Paciente(nombre, apellido, dni, "paciente", contacto, obraSocial, false, salaId));
			}
		} catch (SQLException e) {
			System.err.println("Error al obtener pacientes: " + e.getMessage());
		} finally {
			desconectar();
		}
		return pacientes;
	}

	public boolean asignarSalaPaciente(String pacienteDni, Integer salaId) {
		conectar();
		String salaIdValue;
		if (salaId == null || salaId == 0) {
			salaIdValue = "0";
		} else {
			salaIdValue = String.valueOf(salaId);
		}

		String sql = "UPDATE Paciente SET sala_id = " + salaIdValue + " WHERE usuario_dni = '" + pacienteDni + "'";

		try (Statement stmt = conn.createStatement()) {
			int filas = stmt.executeUpdate(sql);
			if (filas > 0) {
				System.out.println("Sala asignada/desasignada al paciente exitosamente.");
			} else {
				System.err.println("No se encontró el paciente para asignar/desasignar sala o no hubo cambios.");
			}
			return filas > 0;
		} catch (SQLException e) {
			System.err.println("Error al asignar sala al paciente: " + e.getMessage());
			return false;
		} finally {
			desconectar();
		}
	}

	public boolean agregarDiagnostico(Diagnostico diagnostico) {
		try {
			conectar();
			Statement stmt = conn.createStatement();

			String sql = "INSERT INTO diagnostico (paciente_dni, medico_dni, descripcion, fecha) VALUES ('"
					+ diagnostico.getPacienteId() + "', '" + diagnostico.getMedicoId() + "', '"
					+ diagnostico.getDescripcion() + "', '" + diagnostico.getFecha().toString() + "')";

			int filas = stmt.executeUpdate(sql);
			return filas > 0;
		} catch (SQLException e) {
			System.err.println("Error al agregar diagnóstico: " + e.getMessage());
			e.printStackTrace();
			return false;
		} finally {
			desconectar();
		}
	}

	// -------------- GESTION DE SALAS --------------

	public boolean agregarSala(String tipo, boolean disponibilidad) {
		try {
			conectar();
			Statement stmt = conn.createStatement();

			String sql = "INSERT INTO Sala (tipo, disponibilidad) VALUES ('" + tipo + "', " + (disponibilidad ? 1 : 0)
					+ ")";
			int filas = stmt.executeUpdate(sql);

			return filas > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			desconectar();
		}
	}

	public boolean modificarSala(int id, String nuevoTipo, boolean nuevaDisponibilidad) {
		try {
			conectar();
			Statement stmt = conn.createStatement();

			String sql = "UPDATE Sala SET tipo = '" + nuevoTipo + "', disponibilidad = " + (nuevaDisponibilidad ? 1 : 0)
					+ " WHERE id = " + id;
			int filas = stmt.executeUpdate(sql);

			return filas > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			desconectar();
		}
	}

	public boolean eliminarSala(int id) {
		try {
			conectar();
			Statement stmt = conn.createStatement();

			String sql = "DELETE FROM Sala WHERE id = " + id;
			int filas = stmt.executeUpdate(sql);

			return filas > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			desconectar();
		}
	}

	public List<Sala> obtenerTodasLasSalas() {
		List<Sala> salas = new ArrayList<>();
		conectar();
		String sql = "SELECT id, tipo, disponibilidad FROM Sala";
		try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

			while (rs.next()) {
				int id = rs.getInt("id");
				String tipo = rs.getString("tipo");
				boolean disponibilidad = rs.getBoolean("disponibilidad");

				salas.add(new Sala(id, tipo, disponibilidad));
			}
		} catch (SQLException e) {
			System.err.println("Error al obtener salas: " + e.getMessage());
		} finally {
			desconectar();
		}
		return salas;
	}

	public boolean agregarTurnoMantenimiento(String empleadoDni, int salaId, Date fecha) {
		try {
			conectar();
			Statement stmt = conn.createStatement();

			String sql = "INSERT INTO TurnoMantenimiento (empleado_dni, sala_id, fecha, limpia) VALUES ('" + empleadoDni
					+ "', " + salaId + ", '" + fecha.toString() + "', " + 0 + ")";
			int filas = stmt.executeUpdate(sql);

			return filas > 0;
		} catch (SQLException e) {
			System.err.println("Error al agregar turno de mantenimiento: " + e.getMessage());
			e.printStackTrace();
			return false;
		} finally {
			desconectar();
		}
	}

	// ########################### CLASE ADMINISTRATIVO ###########################

	public List<Turno> obtenerTodosLosTurnos() {
		List<Turno> turnos = new ArrayList<>();
		conectar();
		try {
			Statement stat = conn.createStatement();
			String sql = "SELECT id,empleado_dni, paciente_dni, dia, hora_inicio, hora_fin FROM turno";
			ResultSet rs = stat.executeQuery(sql);
			while (rs.next()) {
				int id = rs.getInt("id");
				String empleadoDNI = rs.getString("empleado_dni");
				String pacienteDNI = rs.getString("paciente_dni");
				String fecha = rs.getString("dia");
				LocalDate fechaCambiada = transformarFecha(fecha);

				String hora_inicio = rs.getString("hora_inicio");
				LocalTime hora_inicioCambiada = transformarHora(hora_inicio);

				String hora_fin = rs.getString("hora_fin");
				LocalTime hora_finCambiada = transformarHora(hora_fin);

				turnos.add(
						new Turno(id, empleadoDNI, pacienteDNI, fechaCambiada, hora_inicioCambiada, hora_finCambiada));

			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			desconectar();
		}

		return turnos;
	}

	public LocalTime transformarHora(String horas) {
		int hora = Integer.parseInt(horas.substring(0, horas.indexOf(":")));
		int min = Integer.parseInt(horas.substring(horas.indexOf(":") + 1, horas.lastIndexOf(":")));
		int seg = Integer.parseInt(horas.substring(horas.lastIndexOf(":") + 1));
		return LocalTime.of(hora, min, seg);
	}

	public LocalDate transformarFecha(String fecha) {
		int anio = Integer.parseInt(fecha.substring(0, fecha.indexOf("-")));
		int mes = Integer.parseInt(fecha.substring(fecha.indexOf("-") + 1, fecha.lastIndexOf("-")));
		int dia = Integer.parseInt(fecha.substring(fecha.lastIndexOf("-") + 1));
		return LocalDate.of(anio, mes, dia);

	}

	// #################################### MEDICO ################################

	public boolean asignarTurno(Turno t) {
		conectar();
		if (conn == null)
			return false;

		String sql = "INSERT INTO turno (empleado_dni, paciente_dni, dia, hora_inicio, hora_fin) VALUES (?, ?, ?, ?, ?)";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, t.getMedicoDni());
			pstmt.setString(2, t.getPacienteDni());
			pstmt.setDate(3, Date.valueOf(t.getDia()));
			pstmt.setTime(4, Time.valueOf(t.getHoraInicio()));
			pstmt.setTime(5, Time.valueOf(t.getHoraFin()));

			int filas = pstmt.executeUpdate();
			if (filas > 0) {
				System.out.println("Turno asignado exitosamente.");
			} else {
				System.err.println("No se pudo asignar el turno.");
			}
			return filas > 0;
		} catch (SQLException e) {
			System.err.println("Error al asignar turno: " + e.getMessage());
			return false;
		} finally {
			desconectar();
		}
	}

	public boolean registrarReceta(Receta r) {
		try {
			conectar();
			Statement stat = conn.createStatement();
			String sql = "INSERT INTO receta(paciente_dni,medico_dni,medicamentos,fecha) VALUE ('" + r.getPacienteId()
					+ "','" + r.getMedicoId() + "','" + r.getMedicamentos() + "','" + r.getFecha() + "')";
			int filas = stat.executeUpdate(sql);
			return filas > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			desconectar();
		}

	}

	public ArrayList<Object[][]> mostrarHistorialPaciente(String idMedico) {
		String dni = "", nombre = "", apellido = "", contacto = "", obra = "", medicamentos = "", fecha_receta = "",
				descripcion = "", fecha_diagnostico = "";
		ArrayList<Object[][]> datosObjectos = new ArrayList<>();
		try {
			conectar();
			Statement stat = conn.createStatement();
			String sql1 = "SELECT usuario.dni, usuario.nombre, usuario.apellido, paciente.contacto, paciente.obra_social, receta.medicamentos, receta.fecha, diagnostico.descripcion, diagnostico.fecha\r\n"
					+ "FROM usuario INNER JOIN paciente\r\n" + "ON usuario.dni=paciente.usuario_dni\r\n"
					+ "INNER JOIN receta\r\n" + "ON paciente.usuario_dni=receta.paciente_dni\r\n"
					+ "INNER JOIN diagnostico\r\n" + "ON paciente.usuario_dni = diagnostico.paciente_dni\r\n"
					+ "INNER JOIN turno\r\n" + "ON paciente.usuario_dni = turno.paciente_dni\r\n"
					+ "WHERE turno.empleado_dni = '" + idMedico + "';";
			Statement stat1 = conn.createStatement();
			ResultSet rs1 = stat1.executeQuery(sql1);

			while (rs1.next()) {
				dni = rs1.getString("usuario.dni");
				nombre = rs1.getString("usuario.nombre");
				apellido = rs1.getString("usuario.apellido");
				contacto = rs1.getString("paciente.contacto");
				obra = rs1.getString("paciente.obra_social");
				medicamentos = rs1.getString("receta.medicamentos");
				fecha_receta = rs1.getString("receta.fecha");
				descripcion = rs1.getString("diagnostico.descripcion");
				fecha_diagnostico = rs1.getString("diagnostico.fecha");
				Object[][] datos = { { dni, nombre, apellido, contacto, obra, medicamentos, fecha_receta, descripcion,
						fecha_diagnostico } };
				datosObjectos.add(datos);
			}
			rs1.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return datosObjectos;
	}

	public ArrayList<Object[][]> pacientesAsignados(String idMedico) {
		String dni = "", nombre = "", apellido = "", contacto = "", obra = "";
		int salaId = 0;
		ArrayList<Object[][]> datosObjectos = new ArrayList<>();
		try {
			conectar();
			Statement stat = conn.createStatement();
			String sql1 = "SELECT usuario.dni, usuario.nombre, usuario.apellido, paciente.contacto, paciente.obra_social, paciente.sala_id\r\n"
					+ "FROM usuario INNER JOIN paciente\r\n" + "ON usuario.dni=paciente.usuario_dni\r\n"
					+ "INNER JOIN turno\r\n" + "ON paciente.usuario_dni = turno.paciente_dni\r\n"
					+ "WHERE turno.empleado_dni = '" + idMedico + "';";
			Statement stat1 = conn.createStatement();
			ResultSet rs1 = stat1.executeQuery(sql1);

			while (rs1.next()) {
				dni = rs1.getString("usuario.dni");
				nombre = rs1.getString("usuario.nombre");
				apellido = rs1.getString("usuario.apellido");
				contacto = rs1.getString("paciente.contacto");
				obra = rs1.getString("paciente.obra_social");
				salaId = rs1.getInt("paciente.sala_id");
				Object[][] datos = {
						{ dni, nombre, apellido, contacto, obra, salaId == 0 ? "Libre" : String.valueOf(salaId) } };
				datosObjectos.add(datos);
			}
			rs1.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return datosObjectos;
	}

	public String verificarRol(String dni) {
		conectar();
		String rol = "";
		try {
			Statement stat = conn.createStatement();
			String sql = "SELECT rol FROM usuario WHERE dni='" + dni + "';";
			ResultSet rs = stat.executeQuery(sql);

			if (rs.next()) {
				rol = rs.getString("rol");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rol;
	}

	public List<Turno> obtenerCitasMedicos(String idMedico) {
		List<Turno> turnos = new ArrayList<>();
		conectar();
		try {
			Statement stat = conn.createStatement();
			String sql = "SELECT id,paciente_dni, dia, hora_inicio, hora_fin FROM turno WHERE empleado_dni='" + idMedico
					+ "';";
			ResultSet rs = stat.executeQuery(sql);
			while (rs.next()) {
				int id = rs.getInt("id");
				String pacienteDNI = rs.getString("paciente_dni");
				String fecha = rs.getString("dia");
				LocalDate fechaCambiada = transformarFecha(fecha);

				String hora_inicio = rs.getString("hora_inicio");
				LocalTime hora_inicioCambiada = transformarHora(hora_inicio);

				String hora_fin = rs.getString("hora_fin");
				LocalTime hora_finCambiada = transformarHora(hora_fin);

				turnos.add(new Turno(id, idMedico, pacienteDNI, fechaCambiada, hora_inicioCambiada, hora_finCambiada));

			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			desconectar();
		}

		return turnos;
	}

	// #################################### MANTENIMIENTO
	// ################################

	public List<TurnoMantenimientoInfo> obtenerTurnosMantenimientoPorDNI(String empleadoDni) {
		List<TurnoMantenimientoInfo> turnosMantenimiento = new ArrayList<>();
		Statement stmt = null;
		ResultSet rs = null;

		try {
			conectar();
			String sql = "SELECT tm.sala_id, s.tipo, tm.limpia, tm.fecha " + "FROM TurnoMantenimiento tm "
					+ "JOIN Sala s ON tm.sala_id = s.id " + "WHERE tm.empleado_dni = '" + empleadoDni + "'";

			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);

			while (rs.next()) {
				int salaId = rs.getInt("sala_id");
				String tipoSala = rs.getString("tipo");
				boolean limpia = rs.getBoolean("limpia");
				Date fechaMantenimiento = rs.getDate("fecha");

				TurnoMantenimientoInfo info = new TurnoMantenimientoInfo(salaId, tipoSala, limpia, fechaMantenimiento);
				turnosMantenimiento.add(info);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			desconectar();
		}
		return turnosMantenimiento;
	}

	public boolean actualizarEstadoLimpiezaSala(int salaId, boolean estadoLimpia) {
		Statement stmt = null;
		try {
			conectar();
			stmt = conn.createStatement();

			String sql = "UPDATE TurnoMantenimiento SET limpia = " + (estadoLimpia ? 1 : 0) + " WHERE sala_id = "
					+ salaId;

			int filasAfectadas = stmt.executeUpdate(sql);
			return filasAfectadas > 0;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			desconectar();
		}
	}

	// ################################################ METODOS DE ENFERMERO
	// ################################################################//
	public List<Integer> mostrarCama() {
		List<Integer> salas = new ArrayList<>();
		int id = 0;
		conectar();

		try {
			Statement stat = conn.createStatement();
			String sql = "SELECT id FROM sala WHERE disponibilidad=1 AND tipo = 'Habitacion' ";
			ResultSet rs = stat.executeQuery(sql);
			while (rs.next()) {
				id = rs.getInt("id");
				salas.add(id);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			desconectar();
		}
		return salas;
	}

	public List<Paciente> mostrarPacientesSinCama() {
		List<Paciente> paciente = new ArrayList<>();
		String id = "";
		String nombre = "";
		String apellido = "";
		conectar();

		try {
			Statement stat = conn.createStatement();
			String sql = "SELECT u.dni, u.nombre, u.apellido FROM usuario u INNER JOIN paciente p ON u.dni = p.usuario_dni WHERE p.alta=0 AND p.sala_id=0;";
			ResultSet rs = stat.executeQuery(sql);
			while (rs.next()) {
				id = rs.getString("u.dni");
				nombre = rs.getString("u.nombre");
				apellido = rs.getString("u.apellido");
				Paciente p = new Paciente(nombre, apellido, id, null, null, null, false, 0);
				paciente.add(p);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			desconectar();
		}
		return paciente;
	}

	public List<Paciente> mostrarPacientesDeBaja() {
		List<Paciente> paciente = new ArrayList<>();
		String id = "";
		String nombre = "";
		String apellido = "";
		int cama = 0;
		conectar();

		try {
			Statement stat = conn.createStatement();
			String sql = "SELECT u.dni, u.nombre, u.apellido, p.sala_id FROM usuario u INNER JOIN paciente p ON u.dni = p.usuario_dni WHERE p.alta=0 AND p.sala_id!=0;";
			ResultSet rs = stat.executeQuery(sql);
			while (rs.next()) {
				id = rs.getString("u.dni");
				nombre = rs.getString("u.nombre");
				apellido = rs.getString("u.apellido");
				cama = rs.getInt("p.sala_id");
				Paciente p = new Paciente(nombre, apellido, id, null, null, null, false, cama);
				paciente.add(p);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			desconectar();
		}
		return paciente;
	}

	public boolean actualizarDisponibilidad1(String id_sala) {
		conectar();
		int resultado = 0;
		try {

			Statement stat = conn.createStatement();
			String sql = "UPDATE sala SET disponibilidad=0 WHERE id=" + id_sala + ";";
			resultado = stat.executeUpdate(sql);

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			desconectar();
		}
		if (resultado > 0) {
			return true;
		} else {
			return false;
		}

	}

	public boolean darAlta(String dni) {
		conectar();
		int resultado = 0;
		try {
			Statement stat = conn.createStatement();
			String sql = "UPDATE paciente SET alta=1 WHERE usuario_dni='" + dni + "';";
			resultado = stat.executeUpdate(sql);

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			desconectar();
		}
		if (resultado > 0) {
			return true;
		} else {
			return false;
		}

	}

	public boolean actualizarPacienteSala(String dni) {
		conectar();
		int resultado = 0;
		try {
			Statement stat = conn.createStatement();
			String sql = "UPDATE paciente SET sala_id=0 WHERE usuario_dni='" + dni + "';";
			resultado = stat.executeUpdate(sql);

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			desconectar();
		}
		if (resultado > 0) {
			return true;
		} else {
			return false;
		}

	}

	public boolean actualizarDisponibilidad2(String dni) {
		conectar();
		int resultado = 0;
		String id_sala = "";
		try {

			Statement stat = conn.createStatement();
			String sql1 = "SELECT sala_id FROM paciente WHERE usuario_dni='" + dni + "';";
			ResultSet rs = stat.executeQuery(sql1);
			if (rs.next()) {
				id_sala = rs.getString("sala_id");
			}
			String sql = "UPDATE sala SET disponibilidad=1 WHERE id=" + id_sala + ";";
			resultado = stat.executeUpdate(sql);

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			desconectar();
		}
		if (resultado > 0) {
			return true;
		} else {
			return false;
		}

	}

}