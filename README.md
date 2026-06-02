# NightBite

NightBite es un videojuego móvil 2D de terror psicológico y comedia desarrollado para la clase de POO II.

El jugador toma el papel de un repartidor nocturno atrapado en una dimensión paranormal después de aceptar un pedido extraño dentro de una aplicación de delivery. Durante la partida, deberá completar entregas, evitar monstruos y trampas, acumular puntaje y sobrevivir antes de que termine el tiempo.

## Género

Terror psicológico • Arcade • Comedia • Exploración 2D

## Plataforma

Android

## Descripción general del proyecto

El proyecto se organiza como una aplicación cliente-servidor:

* El frontend móvil se desarrolla en Android Studio.
* El backend se desarrolla con Spring Boot en IntelliJ IDEA.
* La base de datos utiliza PostgreSQL.
* La app móvil consume la API mediante Retrofit.
* Room se utiliza para almacenamiento local en el dispositivo.
* La lógica del videojuego incluye movimiento del jugador, pedidos, puntaje, vidas, tiempo límite e inteligencia artificial básica para los monstruos.

## Tecnologías principales

### Frontend móvil

* Android Studio
* Kotlin
* Jetpack Compose
* Material Design
* Retrofit
* Room
* LibGDX
* Tiled Map Editor

### Backend

* IntelliJ IDEA
* Java
* Spring Boot
* Spring Web
* Spring Data JPA
* PostgreSQL Driver
* Lombok
* Maven

### Base de datos

* PostgreSQL

### Herramientas de prueba y documentación

* Postman
* GitHub
* Git

## Estructura del repositorio

```text
NightBite_JuegoMovil/
│
├── assets_design/
│   └── Recursos visuales, bocetos, referencias, personajes, mapas y elementos gráficos del proyecto.
│
├── backend/
│   └── Proyecto de API REST desarrollado con Spring Boot en IntelliJ IDEA.
│
├── database/
│   └── Scripts SQL, modelo de base de datos, inserts de prueba y diagramas relacionados con PostgreSQL.
│
├── docs/
│   └── Documentación del proyecto, avances, manual técnico, manual de usuario, diagramas y evidencias.
│
├── mobile/
│   └── Proyecto móvil desarrollado en Android Studio.
│
├── .gitignore
│   └── Archivo para excluir configuraciones locales, archivos generados, builds y datos sensibles.
│
└── README.md
    └── Descripción general del proyecto y estructura del repositorio.
```

## Explicación de carpetas

### `assets_design/`

Contiene recursos de diseño que sirven como apoyo visual para el proyecto. Aquí pueden almacenarse bocetos, imágenes de referencia, diseños de personajes, mapas preliminares, paleta de colores y elementos gráficos antes de integrarlos al proyecto móvil.

### `backend/`

Contiene la API REST del proyecto. Esta API se encargará de gestionar los datos del jugador, progreso, puntajes y otra información que deba guardarse en la base de datos.

El backend se desarrolla con Spring Boot y se conecta a PostgreSQL mediante Spring Data JPA.

### `database/`

Contiene los scripts relacionados con la base de datos PostgreSQL. Aquí se pueden guardar archivos como:

* creación de tablas;
* inserts de prueba;
* consultas SQL;
* diagramas entidad-relación;
* respaldo de estructura de base de datos.

### `docs/`

Contiene la documentación académica y técnica del proyecto. Esta carpeta puede incluir:

* propuesta del proyecto;
* diagramas de arquitectura;
* diagramas de flujo;
* avances semanales;
* capturas de evidencia;
* manual técnico;
* manual de usuario.

### `mobile/`

Contiene el frontend móvil del videojuego. Este proyecto se desarrolla en Android Studio y representa la aplicación que usará el jugador.

Aquí se implementan las pantallas, navegación, conexión con la API mediante Retrofit, almacenamiento local con Room y la lógica del juego.

## Arquitectura general

```text
Aplicación Android
        ↓
Retrofit
        ↓
API REST Spring Boot
        ↓
Spring Data JPA
        ↓
PostgreSQL
```

Room se utiliza como almacenamiento local dentro del dispositivo para guardar datos temporales o progreso local, especialmente cuando no haya conexión inmediata con el backend.

## Funcionalidades principales esperadas

* Registro e inicio de sesión del jugador.
* Consulta y guardado del progreso.
* Registro de puntajes.
* Ranking o historial de partidas.
* Juego 2D tipo arcade.
* Sistema de vidas.
* Tiempo límite por partida.
* Entregas de pedidos.
* Monstruos con inteligencia artificial básica.
* Trampas dentro del mapa.
* Puntaje acumulativo según entregas realizadas.

## Estado del proyecto

Proyecto en desarrollo.

Actualmente se está organizando la estructura base del repositorio, separando frontend móvil, backend, base de datos, documentación y recursos de diseño.
