-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 31-05-2025 a las 16:36:42
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `appcarloshaya`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `diagnostico`
--

CREATE TABLE `diagnostico` (
  `id` int(11) NOT NULL,
  `paciente_dni` varchar(20) NOT NULL,
  `medico_dni` varchar(20) NOT NULL,
  `descripcion` text NOT NULL,
  `fecha` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `diagnostico`
--

INSERT INTO `diagnostico` (`id`, `paciente_dni`, `medico_dni`, `descripcion`, `fecha`) VALUES
(6, '30123456', '3333', 'Consulta general, paciente presenta síntomas de resfriado común.', '2025-05-31'),
(7, 'X1234567A', '3333', 'Dolor de cabeza recurrente, se recomienda realizar estudios complementarios.', '2025-05-31'),
(8, '31234567', '3333', 'Control de rutina, paciente en buen estado de salud general.', '2025-05-31'),
(9, 'Y2345678B', '3333', 'Fiebre y malestar general, posible infección viral.', '2025-05-31'),
(10, '32345678', '3333', 'Revisión por alergias estacionales, se ajusta medicación.', '2025-05-31'),
(11, 'Z3456789C', '3333', 'Control post-operatorio, evolución favorable.', '2025-05-31'),
(12, '33456789', '3333', 'Paciente con síntomas gastrointestinales, se prescribe dieta blanda.', '2025-05-31'),
(13, 'A4567890D', '3333', 'Chequeo anual, resultados de laboratorio dentro de rangos normales.', '2025-05-31'),
(14, '34567890', '3333', 'Dolor de espalda crónico, se sugiere fisioterapia.', '2025-05-31'),
(15, 'B5678901E', '3333', 'Insomnio, se recomienda higiene del sueño y seguimiento.', '2025-05-31'),
(16, '35678901', '3333', 'Erupción cutánea, posible reacción alérgica, se prescribe antihistamínico.', '2025-05-31'),
(17, 'C6789012F', '3333', 'Control de presión arterial, se ajusta medicación si es necesario.', '2025-05-31'),
(18, '36789012', '3333', 'Infección de garganta, se prescribe antibiótico.', '2025-05-31'),
(19, 'D7890123G', '3333', 'Lesión deportiva en la rodilla, se deriva a traumatología.', '2025-05-31'),
(20, '37890123', '3333', 'Chequeo de diabetes, niveles de glucosa controlados.', '2025-05-31'),
(21, 'E8901234H', '3333', 'Síntomas de ansiedad, se recomienda terapia psicológica.', '2025-05-31'),
(22, '38901234', '3333', 'Control de colesterol, se ajusta dieta.', '2025-05-31'),
(23, 'F9012345I', '3333', 'Problemas de visión, se deriva a oftalmología.', '2025-05-31'),
(24, '39012345', '3333', 'Examen dental, sin caries, se recomienda limpieza.', '2025-05-31'),
(25, 'G0123456J', '3333', 'Revisión por fatiga crónica, se solicitan análisis de sangre.', '2025-05-31');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `empleado`
--

CREATE TABLE `empleado` (
  `usuario_dni` varchar(20) NOT NULL,
  `contrasena` varchar(255) NOT NULL,
  `sala_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `empleado`
--

INSERT INTO `empleado` (`usuario_dni`, `contrasena`, `sala_id`) VALUES
('00112233L', 'manten123', 27),
('1111', '1111', NULL),
('11223344C', 'admin123', NULL),
('12345678A', 'admin123', NULL),
('20123456', 'admin123', NULL),
('21234567', 'admin123', NULL),
('2222', '2222', NULL),
('22334455D', 'adminis123', NULL),
('22345678', 'adminis123', NULL),
('23456789', 'adminis123', NULL),
('24567890', 'enfermero123', 30),
('25678901', 'enfermero123', 35),
('26789012', 'manten123', 28),
('27890123', 'manten123', 39),
('3333', '3333', 33),
('33445566E', 'adminis123', NULL),
('4444', '4444', 32),
('44556677F', 'adminis123', NULL),
('5555', '5555', 29),
('55667788G', 'enfermero123', 36),
('66778899H', 'enfermero123', 34),
('77889900I', 'enfermero123', 31),
('88990011J', 'manten123', 38),
('98765432B', 'admin123', NULL),
('99001122K', 'manten123', 37);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `paciente`
--

CREATE TABLE `paciente` (
  `usuario_dni` varchar(20) NOT NULL,
  `contacto` varchar(100) DEFAULT NULL,
  `obra_social` varchar(100) DEFAULT NULL,
  `alta` tinyint(1) NOT NULL,
  `sala_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `paciente`
--

INSERT INTO `paciente` (`usuario_dni`, `contacto`, `obra_social`, `alta`, `sala_id`) VALUES
('30123456', 'laura.garcia@email.com', 'OSDE', 1, 0),
('31234567', 'sofia.ramirez@email.com', 'Galeno', 1, 0),
('32345678', 'valentina.perez@email.com', 'Prevención Salud', 1, 0),
('33456789', 'camila.torres@email.com', 'Swiss Medical', 0, 0),
('34567890', 'victoria.flores@email.com', 'Medicus', 1, 0),
('35678901', 'julieta.luna@email.com', 'OSDE', 1, 0),
('36789012', 'antonella.rojas@email.com', 'Galeno', 0, 0),
('37890123', 'brenda.herrera@email.com', 'Prevención Salud', 1, 0),
('38901234', 'carolina.iglesias@email.com', 'Swiss Medical', 1, 0),
('39012345', 'florencia.gil@email.com', 'Medicus', 0, 0),
('A4567890D', 'juan.ruiz@email.com', 'Galeno', 1, 0),
('B5678901E', 'santiago.vega@email.com', 'Prevención Salud', 0, 0),
('C6789012F', 'facundo.silva@email.com', 'Swiss Medical', 1, 0),
('D7890123G', 'gabriel.castro@email.com', 'Medicus', 1, 0),
('E8901234H', 'esteban.moreno@email.com', 'OSDE', 0, 0),
('F9012345I', 'ricardo.navarro@email.com', 'Galeno', 1, 0),
('G0123456J', 'manuel.sanchez@email.com', 'Prevención Salud', 1, 0),
('X1234567A', 'diego.fernandez@email.com', 'Swiss Medical', 1, 0),
('Y2345678B', 'martin.diaz@email.com', 'Medicus', 0, 0),
('Z3456789C', 'lucas.gomez@email.com', 'OSDE', 1, 0);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `receta`
--

CREATE TABLE `receta` (
  `id` int(11) NOT NULL,
  `paciente_dni` varchar(20) NOT NULL,
  `medico_dni` varchar(20) NOT NULL,
  `medicamentos` text NOT NULL,
  `fecha` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `receta`
--

INSERT INTO `receta` (`id`, `paciente_dni`, `medico_dni`, `medicamentos`, `fecha`) VALUES
(6, '30123456', '3333', 'Ibuprofeno 400mg, 1 cada 8 horas por 5 días.', '2025-05-31'),
(7, 'X1234567A', '3333', 'Paracetamol 500mg, 1 cada 6 horas según necesidad.', '2025-05-31'),
(8, '31234567', '3333', 'No se requieren medicamentos, control de rutina.', '2025-05-31'),
(9, 'Y2345678B', '3333', 'Antipirético (ej. Ibuprofeno) y reposo.', '2025-05-31'),
(10, '32345678', '3333', 'Desloratadina 5mg, 1 al día por 10 días.', '2025-05-31'),
(11, 'Z3456789C', '3333', 'Analgésico suave (ej. Paracetamol) según necesidad.', '2025-05-31'),
(12, '33456789', '3333', 'Sales de rehidratación oral y dieta blanda.', '2025-05-31'),
(13, 'A4567890D', '3333', 'No se requieren medicamentos.', '2025-05-31'),
(14, '34567890', '3333', 'Relajante muscular (ej. Diazepam) 5mg, 1 antes de dormir por 7 días.', '2025-05-31'),
(15, 'B5678901E', '3333', 'Melatonina 3mg, 1 antes de dormir.', '2025-05-31'),
(16, '35678901', '3333', 'Cetirizina 10mg, 1 al día por 7 días.', '2025-05-31'),
(17, 'C6789012F', '3333', 'Amlodipino 5mg, 1 al día.', '2025-05-31');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `sala`
--

CREATE TABLE `sala` (
  `id` int(11) NOT NULL,
  `tipo` varchar(50) NOT NULL,
  `disponibilidad` tinyint(1) NOT NULL DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `sala`
--

INSERT INTO `sala` (`id`, `tipo`, `disponibilidad`) VALUES
(7, 'habitacion', 1),
(8, 'habitacion', 1),
(9, 'habitacion', 1),
(10, 'habitacion', 1),
(11, 'habitacion', 1),
(12, 'habitacion', 1),
(13, 'habitacion', 1),
(14, 'habitacion', 1),
(15, 'habitacion', 1),
(16, 'habitacion', 1),
(17, 'habitacion', 1),
(18, 'habitacion', 1),
(19, 'habitacion', 1),
(20, 'habitacion', 1),
(21, 'habitacion doble', 1),
(22, 'habitacion doble', 1),
(23, 'habitacion doble', 1),
(24, 'habitacion doble', 1),
(25, 'habitacion doble', 1),
(26, 'habitacion doble', 1),
(27, 'quirofano', 0),
(28, 'urgencias', 0),
(29, 'consultorio', 0),
(30, 'laboratorio', 0),
(31, 'radiologia', 0),
(32, 'uci', 0),
(33, 'farmacia', 0),
(34, 'sala_espera', 0),
(35, 'fisioterapia', 0),
(36, 'maternidad', 0),
(37, 'pediatria', 0),
(38, 'psiquiatria', 1),
(39, 'unidad_coronaria', 1),
(40, 'dialisis', 1),
(41, 'recuperacion', 1),
(42, 'esterilizacion', 1),
(43, 'almacen', 1),
(44, 'administracion', 1),
(45, 'cafeteria', 1),
(46, 'morgue', 1);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `turno`
--

CREATE TABLE `turno` (
  `id` int(11) NOT NULL,
  `empleado_dni` varchar(20) NOT NULL,
  `paciente_dni` varchar(20) NOT NULL,
  `dia` date NOT NULL,
  `hora_inicio` time NOT NULL,
  `hora_fin` time NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `turno`
--

INSERT INTO `turno` (`id`, `empleado_dni`, `paciente_dni`, `dia`, `hora_inicio`, `hora_fin`) VALUES
(9, '3333', '30123456', '2025-05-31', '09:00:00', '09:30:00'),
(10, '3333', 'X1234567A', '2025-05-31', '09:30:00', '10:00:00'),
(11, '3333', '31234567', '2025-05-31', '10:00:00', '10:30:00'),
(12, '3333', 'Y2345678B', '2025-05-31', '10:30:00', '11:00:00'),
(13, '3333', '32345678', '2025-06-02', '09:00:00', '09:30:00'),
(14, '3333', 'Z3456789C', '2025-06-02', '09:30:00', '10:00:00'),
(15, '3333', '33456789', '2025-06-02', '10:00:00', '10:30:00'),
(16, '3333', 'A4567890D', '2025-06-02', '10:30:00', '11:00:00'),
(17, '3333', '34567890', '2025-06-03', '14:00:00', '14:30:00'),
(18, '3333', 'B5678901E', '2025-06-03', '14:30:00', '15:00:00'),
(19, '3333', '35678901', '2025-06-03', '15:00:00', '15:30:00'),
(20, '3333', 'C6789012F', '2025-06-03', '15:30:00', '16:00:00'),
(21, '3333', '36789012', '2025-06-04', '09:00:00', '09:30:00'),
(22, '3333', 'D7890123G', '2025-06-04', '09:30:00', '10:00:00'),
(23, '3333', '37890123', '2025-06-04', '10:00:00', '10:30:00'),
(24, '3333', 'E8901234H', '2025-06-04', '10:30:00', '11:00:00'),
(25, '3333', '38901234', '2025-06-05', '14:00:00', '14:30:00'),
(26, '3333', 'F9012345I', '2025-06-05', '14:30:00', '15:00:00'),
(27, '3333', '39012345', '2025-06-05', '15:00:00', '15:30:00'),
(28, '3333', 'G0123456J', '2025-06-05', '15:30:00', '16:00:00');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `turnomantenimiento`
--

CREATE TABLE `turnomantenimiento` (
  `id` int(11) NOT NULL,
  `empleado_dni` varchar(20) NOT NULL,
  `sala_id` int(11) NOT NULL,
  `fecha` date NOT NULL,
  `limpia` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `turnomantenimiento`
--

INSERT INTO `turnomantenimiento` (`id`, `empleado_dni`, `sala_id`, `fecha`, `limpia`) VALUES
(15, '5555', 7, '2025-05-31', 1),
(16, '5555', 8, '2025-05-31', 0),
(17, '5555', 27, '2025-05-31', 0),
(18, '5555', 28, '2025-05-31', 0),
(19, '5555', 9, '2025-06-01', 0),
(20, '5555', 10, '2025-06-01', 0),
(21, '5555', 29, '2025-06-01', 0),
(22, '5555', 30, '2025-06-01', 0),
(23, '5555', 11, '2025-06-02', 0),
(24, '5555', 12, '2025-06-02', 0),
(25, '5555', 31, '2025-06-02', 0),
(26, '5555', 32, '2025-06-02', 0),
(27, '5555', 13, '2025-06-03', 0),
(28, '5555', 14, '2025-06-03', 0),
(29, '5555', 33, '2025-06-03', 0),
(30, '5555', 34, '2025-06-03', 0),
(31, '5555', 15, '2025-06-04', 0),
(32, '5555', 16, '2025-06-04', 0),
(33, '5555', 35, '2025-06-04', 0),
(34, '5555', 36, '2025-06-04', 0);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuario`
--

CREATE TABLE `usuario` (
  `dni` varchar(20) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `apellido` varchar(100) NOT NULL,
  `rol` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `usuario`
--

INSERT INTO `usuario` (`dni`, `nombre`, `apellido`, `rol`) VALUES
('00112233L', 'Javier', 'Ortega', 'medico'),
('1111', 'Juan', 'Pérez', 'administrador'),
('11223344C', 'Andrés', 'López', 'administrador'),
('12345678A', 'Roberto', 'Sánchez', 'administrador'),
('20123456', 'Laura', 'Martínez', 'administrador'),
('21234567', 'Sofía', 'Fernández', 'administrador'),
('2222', 'María', 'González', 'administrativo'),
('22334455D', 'Elena', 'Díaz', 'administrativo'),
('22345678', 'Pedro', 'Ruiz', 'administrativo'),
('23456789', 'Miguel', 'González', 'administrativo'),
('24567890', 'Jorge', 'Jiménez', 'enfermero'),
('25678901', 'Daniel', 'Núñez', 'enfermero'),
('26789012', 'Marta', 'Romero', 'mantenimiento'),
('27890123', 'Silvia', 'Guerrero', 'medico'),
('30123456', 'Laura', 'García', 'paciente'),
('31234567', 'Sofía', 'Ramírez', 'paciente'),
('32345678', 'Valentina', 'Pérez', 'paciente'),
('3333', 'Carlos', 'Rodríguez', 'medico'),
('33445566E', 'Carmen', 'Pérez', 'administrativo'),
('33456789', 'Camila', 'Torres', 'paciente'),
('34567890', 'Victoria', 'Flores', 'paciente'),
('35678901', 'Julieta', 'Luna', 'paciente'),
('36789012', 'Antonella', 'Rojas', 'paciente'),
('37890123', 'Brenda', 'Herrera', 'paciente'),
('38901234', 'Carolina', 'Iglesias', 'paciente'),
('39012345', 'Florencia', 'Gil', 'paciente'),
('4444', 'Ana', 'López', 'enfermero'),
('44556677F', 'Isabel', 'Torres', 'administrativo'),
('5555', 'Pedro', 'Martínez', 'mantenimiento'),
('55667788G', 'Paula', 'Moreno', 'enfermero'),
('66778899H', 'Lucía', 'Blanco', 'enfermero'),
('77889900I', 'Sara', 'Castro', 'enfermero'),
('88990011J', 'Antonio', 'Vázquez', 'mantenimiento'),
('98765432B', 'David', 'García', 'administrador'),
('99001122K', 'Francisco', 'Alonso', 'mantenimiento'),
('A4567890D', 'Juan', 'Ruiz', 'paciente'),
('B5678901E', 'Santiago', 'Vega', 'paciente'),
('C6789012F', 'Facundo', 'Silva', 'paciente'),
('D7890123G', 'Gabriel', 'Castro', 'paciente'),
('E8901234H', 'Esteban', 'Moreno', 'paciente'),
('F9012345I', 'Ricardo', 'Navarro', 'paciente'),
('G0123456J', 'Manuel', 'Sánchez', 'paciente'),
('X1234567A', 'Diego', 'Fernández', 'paciente'),
('Y2345678B', 'Martín', 'Díaz', 'paciente'),
('Z3456789C', 'Lucas', 'Gómez', 'paciente');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `diagnostico`
--
ALTER TABLE `diagnostico`
  ADD PRIMARY KEY (`id`),
  ADD KEY `paciente_dni` (`paciente_dni`),
  ADD KEY `medico_dni` (`medico_dni`);

--
-- Indices de la tabla `empleado`
--
ALTER TABLE `empleado`
  ADD PRIMARY KEY (`usuario_dni`),
  ADD KEY `fk_empleado_sala` (`sala_id`);

--
-- Indices de la tabla `paciente`
--
ALTER TABLE `paciente`
  ADD PRIMARY KEY (`usuario_dni`);

--
-- Indices de la tabla `receta`
--
ALTER TABLE `receta`
  ADD PRIMARY KEY (`id`),
  ADD KEY `paciente_dni` (`paciente_dni`),
  ADD KEY `medico_dni` (`medico_dni`);

--
-- Indices de la tabla `sala`
--
ALTER TABLE `sala`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `turno`
--
ALTER TABLE `turno`
  ADD PRIMARY KEY (`id`),
  ADD KEY `empleado_dni` (`empleado_dni`),
  ADD KEY `paciente_dni` (`paciente_dni`);

--
-- Indices de la tabla `turnomantenimiento`
--
ALTER TABLE `turnomantenimiento`
  ADD PRIMARY KEY (`id`),
  ADD KEY `empleado_dni` (`empleado_dni`),
  ADD KEY `sala_id` (`sala_id`);

--
-- Indices de la tabla `usuario`
--
ALTER TABLE `usuario`
  ADD PRIMARY KEY (`dni`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `diagnostico`
--
ALTER TABLE `diagnostico`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=26;

--
-- AUTO_INCREMENT de la tabla `receta`
--
ALTER TABLE `receta`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=18;

--
-- AUTO_INCREMENT de la tabla `sala`
--
ALTER TABLE `sala`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=47;

--
-- AUTO_INCREMENT de la tabla `turno`
--
ALTER TABLE `turno`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=29;

--
-- AUTO_INCREMENT de la tabla `turnomantenimiento`
--
ALTER TABLE `turnomantenimiento`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=35;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `diagnostico`
--
ALTER TABLE `diagnostico`
  ADD CONSTRAINT `diagnostico_ibfk_1` FOREIGN KEY (`paciente_dni`) REFERENCES `paciente` (`usuario_dni`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `diagnostico_ibfk_2` FOREIGN KEY (`medico_dni`) REFERENCES `empleado` (`usuario_dni`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `empleado`
--
ALTER TABLE `empleado`
  ADD CONSTRAINT `empleado_ibfk_1` FOREIGN KEY (`usuario_dni`) REFERENCES `usuario` (`dni`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_empleado_sala` FOREIGN KEY (`sala_id`) REFERENCES `sala` (`id`) ON DELETE SET NULL ON UPDATE CASCADE;

--
-- Filtros para la tabla `paciente`
--
ALTER TABLE `paciente`
  ADD CONSTRAINT `paciente_ibfk_1` FOREIGN KEY (`usuario_dni`) REFERENCES `usuario` (`dni`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `receta`
--
ALTER TABLE `receta`
  ADD CONSTRAINT `receta_ibfk_1` FOREIGN KEY (`paciente_dni`) REFERENCES `paciente` (`usuario_dni`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `receta_ibfk_2` FOREIGN KEY (`medico_dni`) REFERENCES `empleado` (`usuario_dni`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `turno`
--
ALTER TABLE `turno`
  ADD CONSTRAINT `turno_ibfk_1` FOREIGN KEY (`empleado_dni`) REFERENCES `empleado` (`usuario_dni`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `turno_ibfk_2` FOREIGN KEY (`paciente_dni`) REFERENCES `paciente` (`usuario_dni`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `turnomantenimiento`
--
ALTER TABLE `turnomantenimiento`
  ADD CONSTRAINT `turnomantenimiento_ibfk_1` FOREIGN KEY (`empleado_dni`) REFERENCES `empleado` (`usuario_dni`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `turnomantenimiento_ibfk_2` FOREIGN KEY (`sala_id`) REFERENCES `sala` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
