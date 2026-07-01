# NightBite

**NightBite** es un videojuego móvil 2D desarrollado en Android Studio.

El jugador toma el papel de un repartidor nocturno que queda atrapado en una dimensión paranormal después de aceptar un pedido extraño. Para sobrevivir, debe completar entregas durante distintas jornadas nocturnas, evitar enemigos, administrar el tiempo y regresar al restaurante cuando sea necesario.

El juego combina una estética nocturna tipo arcade con elementos de terror ligero, comedia y mecánicas de entrega bajo presión.

---

## Género

Arcade • Exploración 2D • Juego móvil

---

## Plataforma

Android

La aplicación está diseñada principalmente para jugarse en orientación horizontal.

---

## Descripción general

NightBite funciona como una aplicación móvil con lógica de juego, almacenamiento local y conexión con backend.

La app permite registrar usuarios, iniciar sesión, seleccionar datos iniciales del jugador, jugar diferentes jornadas, guardar progreso local, desbloquear niveles, obtener estrellas, visualizar insignias y consultar logros.

El progreso del jugador se guarda principalmente de forma local usando **Room**, mientras que la sesión y preferencias del juego se manejan con **DataStore**.

---

## Historia del juego

El jugador trabaja como repartidor nocturno. Durante una noche aparentemente normal, acepta un pedido extraño desde una aplicación de delivery y termina atrapado en una dimensión paranormal.

Para avanzar, debe completar jornadas de reparto entre las 12:00 a.m. y las 3:00 a.m., enfrentándose a amenazas cada vez más extrañas. El objetivo es sobrevivir, completar los pedidos y encontrar la forma de volver a casa.

---

## Mecánicas principales

- Movimiento del jugador en un mapa 2D.
- Zona segura ubicada en el restaurante.
- Recogida de pedidos en el restaurante.
- Entrega de pedidos en edificios del mapa.
- Sistema de vidas.
- Sistema de estrellas según pedidos completados.
- Tiempo límite por pedido.
- Tiempo límite para permanecer en la zona segura.
- Tiempo límite para regresar al restaurante después de entregar.
- Tiempo máximo general por nivel.
- Enemigos con rutas y comportamiento básico.
- Colisión con enemigos.
- Desbloqueo progresivo de jornadas.
- Pantalla de resultados.
- Insignias por completar jornadas con 3 estrellas.
- Logros informativos dentro de la pantalla de logros.
- Preferencias de controles y opciones del juego.

---

## Jornadas disponibles

El juego está dividido en cinco jornadas principales:

| Nivel interno | Jornada visual | Nombre |
|--------------|----------------|--------|
| 0 | Tutorial | Tutorial |
| 1 | Jornada 2 | Sombras errantes |
| 2 | Jornada 3 | Lobos callejeros |
| 3 | Jornada 4 | Clientes deformes |
| 4 | Jornada 5 | Repartidores perdidos |

El tutorial introduce la lógica básica del juego. Las jornadas posteriores agregan más dificultad, enemigos y rutas de entrega más exigentes.

---

## Sistema de progreso

El progreso se guarda localmente mediante Room.

Cada jornada registra:

- Nivel desbloqueado.
- Cantidad de estrellas obtenidas.
- Pedidos completados.
- Mejor resultado alcanzado.
- Insignias desbloqueadas.

Las estrellas se calculan según la cantidad de pedidos completados durante la jornada. El resultado máximo obtenido en un nivel se conserva, por lo que una partida posterior con menor puntuación no reduce el progreso anterior.

---

## Sistema de resultados

Al finalizar una jornada, el jugador puede obtener distintos resultados dependiendo de su desempeño:

- Victoria con 3 estrellas.
- Victoria parcial.
- Jornada incompleta.
- Derrota por quedarse sin vidas.
- Derrota por agotarse el tiempo máximo del nivel.

La pantalla de resultados muestra:

- Estrellas obtenidas.
- Pedidos entregados.
- Tiempo usado.
- Mensaje narrativo según el resultado.
- Opciones para continuar, reintentar o volver al inicio.

---

## Tecnologías principales

### Aplicación móvil

- Android Studio
- Kotlin
- Jetpack Compose
- Navigation Compose
- Room
- DataStore
- Retrofit
- Material 3

### Backend

- Java
- Spring Boot
- Spring Web
- Spring Data JPA
- Maven
- API REST

### Herramientas

- Git
- GitHub
- Postman
- Android Emulator
- Dispositivo Android físico para pruebas

### Pantallas principales
- Pantalla de inicio.
- Pantalla de inicio de sesión.
- Pantalla de registro.
- Verificación de edad.
- Selección inicial del jugador.
- Pantalla principal del juego.
- Introducción de nivel.
- Pantalla jugable.
- Pantalla de resultados.
- Pantalla de configuración.
- Pantalla de cuenta.
- Pantalla de logros e insignias.

### Almacenamiento local
NightBite utiliza almacenamiento local para permitir que el progreso del jugador se conserve en el dispositivo.

Se utiliza:
- Room para progreso, niveles, insignias y datos relacionados al juego.
- DataStore para sesión activa y preferencias del usuario.

### Conexión con backend
La aplicación puede comunicarse con una API REST desarrollada en Spring Boot mediante Retrofit. El backend se utiliza para operaciones como:
- Registro de usuarios.
- Inicio de sesión.
- Gestión de datos de cuenta.
- Creación y actualización de información del jugador.

###Estado actual del proyecto
Actualmente cuenta con:
- Flujo de registro e inicio de sesión.
- Persistencia de sesión.
- Progreso local con Room.
- Pantalla principal con niveles desbloqueables.
- Gameplay funcional en mapa 2D.
- Sistema de pedidos.
- Sistema de vidas.
- Sistema de estrellas.
- Enemigos por nivel.
- Pantalla de resultados.
- Insignias desbloqueables.
- Pantalla de logros.
- Configuraciones básicas del juego.
