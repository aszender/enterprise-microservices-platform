# PLAN DE PREPARACIÓN - ENTREVISTA IMPACT.COM
## Viernes 16 de Enero 2026, 10:00 AM

---

# RESUMEN EJECUTIVO

**Tiempo total disponible:** 7 días (Viernes 9 Enero - Jueves 15 Enero)
**Entrevista:** Viernes 16 Enero, 10:00 AM
**Objetivo:** Dominar Java Spring + JavaScript frameworks + System Design básico

---

# DISTRIBUCIÓN DIARIA

| Día | Fecha | Java/Spring | JavaScript/Vue/React | System Design | CELPIP |
|-----|-------|-------------|---------------------|---------------|--------|
| 1 | Vie 9 | 4h | 1h | - | 1h |
| 2 | Sáb 10 | 4h | 2h | 1h | 1h |
| 3 | Dom 11 | 4h | 2h | 1h | 1h |
| 4 | Lun 12 | 3h | 2h | 2h | 1h |
| 5 | Mar 13 | 3h | 2h | 2h | 1h |
| 6 | Mié 14 | 2h | 2h | 2h | 1h |
| 7 | Jue 15 | 2h | 1h | 2h | 1h |

**Viernes 16:** Solo repaso ligero 1h antes de la entrevista

---

# DÍA 1 - VIERNES 9 ENERO
## Tema: Java Fundamentals + Setup

### BLOQUE 1: Java Core (2h) - Mañana/Tarde
**Objetivo:** Recordar sintaxis y conceptos básicos

- [ ] Variables, tipos de datos, operadores
- [ ] Control flow (if/else, switch, loops)
- [ ] Arrays y Collections (List, Map, Set)
- [ ] OOP: Classes, Objects, Inheritance, Polymorphism
- [ ] Interfaces vs Abstract Classes
- [ ] Exception Handling (try/catch/finally)

**Recurso:** https://www.youtube.com/watch?v=eIrMbAQSU34 (Java Full Course - ver en 1.5x, primeras 2h)

### BLOQUE 2: Java 8+ Features (2h) - Tarde
**Objetivo:** Features modernos que usan en empresas

- [ ] Lambda expressions
- [ ] Stream API (map, filter, reduce, collect)
- [ ] Optional class
- [ ] Functional interfaces
- [ ] Method references

**Práctica:** Escribir 10 ejemplos de cada uno

**Recurso:** https://www.youtube.com/watch?v=Q93JsQ8vcwY (Java 8 Features)

### BLOQUE 3: JavaScript Review (1h) - Noche
**Objetivo:** Refrescar JS moderno

- [ ] ES6+ features (arrow functions, destructuring, spread)
- [ ] Promises y async/await
- [ ] Array methods (map, filter, reduce)

### BLOQUE 4: CELPIP (1h) - Noche
- [ ] Práctica de listening o reading

### ✅ CHECKLIST DÍA 1
- [ ] Entiendo OOP en Java
- [ ] Puedo escribir lambdas y usar Streams
- [ ] Recuerdo JavaScript ES6+

---

# DÍA 2 - SÁBADO 10 ENERO
## Tema: Spring Framework Basics

### BLOQUE 1: Spring Core Concepts (2h) - Mañana
**Objetivo:** Entender la base de Spring

- [ ] ¿Qué es Spring Framework?
- [ ] Dependency Injection (DI)
- [ ] Inversion of Control (IoC)
- [ ] Spring Beans y Bean Lifecycle
- [ ] ApplicationContext
- [ ] Annotations: @Component, @Service, @Repository, @Controller

**Recurso:** https://www.youtube.com/watch?v=9SGDpanrc8U (Spring Framework Tutorial)

### BLOQUE 2: Spring Boot (2h) - Tarde
**Objetivo:** Crear aplicación básica

- [ ] ¿Qué es Spring Boot vs Spring?
- [ ] Spring Initializr (start.spring.io)
- [ ] Estructura de proyecto
- [ ] application.properties / application.yml
- [ ] @SpringBootApplication
- [ ] Crear tu primera REST API

**Práctica:** Crear proyecto "hello-world" con endpoint GET /hello

### BLOQUE 3: Vue.js Basics (2h) - Tarde/Noche
**Objetivo:** Entender Vue.js fundamentals

- [ ] Vue instance y data binding
- [ ] Directives (v-if, v-for, v-model, v-on)
- [ ] Components
- [ ] Props y Events
- [ ] Lifecycle hooks

**Recurso:** https://www.youtube.com/watch?v=FXpIoQ_rT_c (Vue 3 Crash Course)

### BLOQUE 4: System Design Intro (1h) - Noche
- [ ] ¿Qué es system design?
- [ ] Conceptos básicos: scalability, availability, latency

### BLOQUE 5: CELPIP (1h) - Noche
- [ ] Práctica de speaking

### ✅ CHECKLIST DÍA 2
- [ ] Entiendo Dependency Injection
- [ ] Creé mi primer proyecto Spring Boot
- [ ] Entiendo Vue.js basics

---

# DÍA 3 - DOMINGO 11 ENERO
## Tema: Spring Boot REST APIs + React

### BLOQUE 1: REST APIs con Spring Boot (2h) - Mañana
**Objetivo:** Dominar creación de APIs

- [ ] @RestController vs @Controller
- [ ] @RequestMapping, @GetMapping, @PostMapping, @PutMapping, @DeleteMapping
- [ ] @PathVariable y @RequestParam
- [ ] @RequestBody y @ResponseBody
- [ ] ResponseEntity
- [ ] HTTP Status codes

**Práctica:** Crear CRUD API para "Products"
```
GET /api/products
GET /api/products/{id}
POST /api/products
PUT /api/products/{id}
DELETE /api/products/{id}
```

### BLOQUE 2: Spring Data JPA (2h) - Tarde
**Objetivo:** Conectar con base de datos

- [ ] ¿Qué es JPA e Hibernate?
- [ ] @Entity, @Table, @Id, @GeneratedValue
- [ ] @Column, @OneToMany, @ManyToOne
- [ ] JpaRepository interface
- [ ] Custom queries con @Query
- [ ] Conectar con PostgreSQL o H2

**Práctica:** Agregar persistencia al CRUD de Products

### BLOQUE 3: React Basics (2h) - Tarde/Noche
**Objetivo:** Entender React fundamentals

- [ ] JSX
- [ ] Components (functional)
- [ ] Props
- [ ] useState hook
- [ ] useEffect hook
- [ ] Event handling

**Recurso:** https://www.youtube.com/watch?v=w7ejDZ8SWv8 (React Crash Course)

### BLOQUE 4: System Design (1h) - Noche
- [ ] Microservices vs Monolith
- [ ] REST API design principles

### BLOQUE 5: CELPIP (1h) - Noche
- [ ] Práctica de writing

### ✅ CHECKLIST DÍA 3
- [ ] Puedo crear CRUD REST API completo
- [ ] Entiendo JPA y puedo conectar a DB
- [ ] Entiendo React hooks

---

# DÍA 4 - LUNES 12 ENERO
## Tema: Spring Security + Testing

### BLOQUE 1: Spring Security Basics (1.5h) - Mañana
**Objetivo:** Entender autenticación/autorización

- [ ] ¿Qué es Spring Security?
- [ ] Authentication vs Authorization
- [ ] SecurityFilterChain
- [ ] @EnableWebSecurity
- [ ] UserDetailsService
- [ ] Password encoding (BCrypt)

**Recurso:** https://www.youtube.com/watch?v=b9O9NI-RJ3o (Spring Security)

### BLOQUE 2: JWT con Spring (1.5h) - Mañana/Tarde
**Objetivo:** Implementar JWT auth

- [ ] ¿Qué es JWT?
- [ ] Estructura de JWT (header, payload, signature)
- [ ] Generar tokens
- [ ] Validar tokens
- [ ] JWT Filter

**Práctica:** Agregar JWT auth a tu API de Products

### BLOQUE 3: Unit Testing (2h) - Tarde
**Objetivo:** Testing en Spring Boot

- [ ] JUnit 5 basics
- [ ] @Test, @BeforeEach, @AfterEach
- [ ] Assertions
- [ ] Mockito (@Mock, @InjectMocks, when/thenReturn)
- [ ] @WebMvcTest para controllers
- [ ] @DataJpaTest para repositories

**Práctica:** Escribir tests para tu ProductController

### BLOQUE 4: System Design (2h) - Tarde/Noche
- [ ] Load balancing
- [ ] Caching (Redis)
- [ ] Database replication

**Práctica:** Diseñar sistema de "URL Shortener"

### BLOQUE 5: CELPIP (1h) - Noche
- [ ] Práctica de listening

### ✅ CHECKLIST DÍA 4
- [ ] Entiendo Spring Security
- [ ] Puedo implementar JWT
- [ ] Puedo escribir unit tests

---

# DÍA 5 - MARTES 13 ENERO
## Tema: Kafka + Databases

### BLOQUE 1: Apache Kafka Concepts (2h) - Mañana
**Objetivo:** Entender messaging

- [ ] ¿Qué es Kafka y para qué sirve?
- [ ] Producers y Consumers
- [ ] Topics y Partitions
- [ ] Consumer Groups
- [ ] Offset management
- [ ] Use cases (event streaming, decoupling)

**Recurso:** https://www.youtube.com/watch?v=Ch5VhJzaoaI (Kafka Crash Course)

### BLOQUE 2: Spring Kafka (1h) - Mañana
**Objetivo:** Kafka en Spring Boot

- [ ] spring-kafka dependency
- [ ] @KafkaListener
- [ ] KafkaTemplate
- [ ] Producer config
- [ ] Consumer config

### BLOQUE 3: Database Deep Dive (2h) - Tarde
**Objetivo:** SQL y database design

- [ ] SQL queries (SELECT, JOIN, GROUP BY, HAVING)
- [ ] Indexes y performance
- [ ] Transactions y ACID
- [ ] N+1 problem en JPA
- [ ] Database normalization

**Práctica:** Escribir 20 queries SQL de práctica

### BLOQUE 4: System Design (2h) - Tarde/Noche
**Objetivo:** Diseñar sistemas

- [ ] Message queues
- [ ] Event-driven architecture
- [ ] API Gateway pattern

**Práctica:** Diseñar "Partnership Tracking System" (relevante para impact.com)

### BLOQUE 5: CELPIP (1h) - Noche
- [ ] Práctica de speaking

### ✅ CHECKLIST DÍA 5
- [ ] Entiendo Kafka y cuándo usarlo
- [ ] Puedo escribir SQL queries complejas
- [ ] Puedo diseñar sistema básico

---

# DÍA 6 - MIÉRCOLES 14 ENERO
## Tema: CI/CD + Cloud + Mock Interview

### BLOQUE 1: Jenkins CI/CD (1h) - Mañana
**Objetivo:** Entender pipelines

- [ ] ¿Qué es CI/CD?
- [ ] Jenkins basics
- [ ] Jenkinsfile
- [ ] Pipeline stages (build, test, deploy)

### BLOQUE 2: Docker (1h) - Mañana
**Objetivo:** Containerización

- [ ] Dockerfile
- [ ] docker build, docker run
- [ ] Docker Compose
- [ ] Dockerizar Spring Boot app

**Práctica:** Crear Dockerfile para tu API

### BLOQUE 3: AWS Basics (2h) - Tarde
**Objetivo:** Cloud fundamentals

- [ ] EC2 basics
- [ ] RDS (PostgreSQL)
- [ ] S3
- [ ] Lambda functions
- [ ] API Gateway

### BLOQUE 4: Mock Interview Técnica (2h) - Tarde/Noche
**Objetivo:** Simular entrevista

Responder en voz alta:
- [ ] "Explain dependency injection"
- [ ] "What's the difference between @Component, @Service, @Repository?"
- [ ] "How does Spring Security work?"
- [ ] "Explain REST API best practices"
- [ ] "What is Kafka and when would you use it?"
- [ ] "How would you design a microservices architecture?"
- [ ] "Explain the difference between Vue and React"

### BLOQUE 5: CELPIP (1h) - Noche
- [ ] Práctica de reading

### ✅ CHECKLIST DÍA 6
- [ ] Entiendo CI/CD y Jenkins
- [ ] Puedo dockerizar una app
- [ ] Practiqué respuestas técnicas en voz alta

---

# DÍA 7 - JUEVES 15 ENERO
## Tema: Final Review + Behavioral + Descanso

### BLOQUE 1: Quick Review (2h) - Mañana
**Objetivo:** Repasar conceptos clave

Repasar mentalmente:
- [ ] Spring annotations y sus usos
- [ ] Java 8 Stream operations
- [ ] REST HTTP methods y status codes
- [ ] Kafka concepts
- [ ] SQL queries comunes

### BLOQUE 2: Behavioral Questions (2h) - Tarde
**Objetivo:** Preparar respuestas STAR

Preparar respuestas para:
- [ ] "Tell me about yourself" (2 min pitch)
- [ ] "Why impact.com?"
- [ ] "Tell me about a challenging project"
- [ ] "How do you handle disagreements with team members?"
- [ ] "Describe a time you had to learn something quickly"
- [ ] "What's your biggest weakness?"
- [ ] "Where do you see yourself in 5 years?"

### BLOQUE 3: Company Research (1h) - Tarde
**Objetivo:** Conocer impact.com

- [ ] Revisar website de impact.com
- [ ] Leer sobre sus productos (Performance, Creator, Advocate)
- [ ] Ver sus clientes (Walmart, Uber, Shopify, etc.)
- [ ] Preparar 3 preguntas para ellos

### BLOQUE 4: System Design Final (2h) - Tarde
- [ ] Repasar diseño de sistemas
- [ ] Practicar explicar arquitecturas en voz alta

### BLOQUE 5: CELPIP (1h) - Tarde
- [ ] Práctica ligera

### BLOQUE 6: Preparación Logística - Noche
- [ ] Preparar ropa
- [ ] Verificar link de videollamada (si es virtual)
- [ ] Preparar ambiente tranquilo
- [ ] Tener agua lista
- [ ] **DORMIR TEMPRANO** (mínimo 7-8 horas)

### ✅ CHECKLIST DÍA 7
- [ ] Repasé todos los conceptos clave
- [ ] Tengo respuestas behavioral listas
- [ ] Investigué impact.com
- [ ] Todo listo para mañana

---

# VIERNES 16 ENERO - DÍA DE LA ENTREVISTA

### 8:00 AM - Despertar
- [ ] Desayuno ligero pero nutritivo
- [ ] Ducha
- [ ] Vestirse profesional (aunque sea virtual)

### 9:00 AM - Warm up (1h)
- [ ] Repasar "Tell me about yourself"
- [ ] Repasar 3 preguntas técnicas clave
- [ ] Respirar profundo, relajarse
- [ ] Pensar positivo: "Estoy preparado"

### 9:45 AM - Preparación final
- [ ] Abrir computadora
- [ ] Verificar cámara y micrófono
- [ ] Cerrar otras aplicaciones
- [ ] Tener agua cerca
- [ ] Tener papel y lápiz

### 10:00 AM - ¡ENTREVISTA! 🎯
- [ ] Sonreír
- [ ] Escuchar bien antes de responder
- [ ] Pedir clarificación si no entiendes
- [ ] Mostrar entusiasmo por impact.com
- [ ] Al final, hacer tus preguntas preparadas

---

# RECURSOS PRINCIPALES

## Videos Esenciales (en orden de prioridad)

| # | Tema | Link | Duración |
|---|------|------|----------|
| 1 | Java 8 Features | https://www.youtube.com/watch?v=Q93JsQ8vcwY | 2h |
| 2 | Spring Boot Tutorial | https://www.youtube.com/watch?v=9SGDpanrc8U | 3h |
| 3 | Spring Security | https://www.youtube.com/watch?v=b9O9NI-RJ3o | 2h |
| 4 | Kafka Crash Course | https://www.youtube.com/watch?v=Ch5VhJzaoaI | 1h |
| 5 | Vue 3 Crash Course | https://www.youtube.com/watch?v=FXpIoQ_rT_c | 2h |
| 6 | React Crash Course | https://www.youtube.com/watch?v=w7ejDZ8SWv8 | 2h |

## Documentación
- **Spring Guides:** https://spring.io/guides
- **Baeldung (EXCELENTE para Spring):** https://www.baeldung.com
- **Vue Docs:** https://vuejs.org/guide/introduction.html
- **React Docs:** https://react.dev/learn

## Práctica de Código
- **Spring Initializr:** https://start.spring.io
- **LeetCode:** https://leetcode.com (Easy problems)

---

# PREGUNTAS TÉCNICAS FRECUENTES + RESPUESTAS

## Java / Spring

### 1. ¿Qué es Dependency Injection?
```
Es un patrón de diseño donde las dependencias de una clase se inyectan 
desde afuera en lugar de crearlas dentro de la clase.

Ejemplo SIN DI:
public class UserService {
    private UserRepository repo = new UserRepository(); // crea su dependencia
}

Ejemplo CON DI:
public class UserService {
    private UserRepository repo;
    
    @Autowired
    public UserService(UserRepository repo) {
        this.repo = repo; // inyectada desde afuera
    }
}

Beneficios: testeable, flexible, bajo acoplamiento.
```

### 2. ¿Diferencia entre @Component, @Service, @Repository, @Controller?
```
Todos son @Component pero con semántica diferente:

@Component  - Genérico, para cualquier bean
@Service    - Capa de lógica de negocio
@Repository - Capa de acceso a datos (agrega traducción de excepciones SQL)
@Controller - Maneja HTTP requests (MVC)
@RestController - @Controller + @ResponseBody (para APIs)

Spring los escanea y crea beans automáticamente.
```

### 3. ¿Qué es Spring Boot vs Spring Framework?
```
Spring Framework: Framework base, requiere mucha configuración XML/Java.

Spring Boot: Capa sobre Spring que:
- Auto-configura todo
- Servidor embebido (Tomcat)
- No necesita XML
- "Convention over configuration"
- Starter dependencies

Ejemplo: spring-boot-starter-web trae todo para web apps.
```

### 4. ¿Cómo funciona Spring Security?
```
1. Request llega al servidor
2. Pasa por cadena de Security Filters
3. Authentication: ¿Quién eres? (username/password, JWT, OAuth)
4. Authorization: ¿Qué puedes hacer? (roles, permissions)

Componentes clave:
- SecurityFilterChain: configura filtros
- UserDetailsService: carga usuarios de DB
- PasswordEncoder: encripta passwords (BCrypt)
- @PreAuthorize: protege métodos
```

### 5. ¿Qué es JPA e Hibernate?
```
JPA (Java Persistence API): Especificación/estándar para ORM en Java.

Hibernate: Implementación más popular de JPA.

ORM: Mapea objetos Java ↔ tablas de base de datos.

@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "product_name")
    private String name;
}
```

### 6. ¿Qué son los Streams en Java 8?
```
API para procesar colecciones de forma funcional y declarativa.

List<String> names = users.stream()
    .filter(u -> u.getAge() > 18)      // filtrar
    .map(User::getName)                 // transformar
    .sorted()                           // ordenar
    .collect(Collectors.toList());      // recolectar

Beneficios: código más limpio, paralelizable, lazy evaluation.
```

## JavaScript / Frameworks

### 7. ¿Diferencia entre Vue y React?
```
Vue:
- Two-way data binding (v-model)
- Templates HTML
- Más opinado, más estructura
- Más fácil de aprender
- Sintaxis: <template>, <script>, <style>

React:
- One-way data flow
- JSX (JavaScript + HTML)
- Más flexible, menos estructura
- Comunidad más grande
- Sintaxis: functional components + hooks

Ambos usan Virtual DOM para performance.
```

### 8. ¿Qué son los React Hooks?
```
Funciones que permiten usar estado y lifecycle en functional components.

useState: estado local
const [count, setCount] = useState(0);

useEffect: side effects (API calls, subscriptions)
useEffect(() => {
    fetchData();
}, [dependency]);

Otros: useContext, useReducer, useMemo, useCallback
```

## System Design

### 9. ¿Qué es Kafka y cuándo usarlo?
```
Apache Kafka: Plataforma de streaming de eventos distribuida.

Conceptos:
- Producer: envía mensajes
- Consumer: recibe mensajes
- Topic: categoría de mensajes
- Partition: división de un topic para escalar
- Consumer Group: grupo de consumers que comparten carga

Cuándo usar:
- Desacoplar microservicios
- Event sourcing
- Procesamiento en tiempo real
- Alta disponibilidad y throughput
- Logs y métricas

Ejemplo: User hace compra → evento a Kafka → 
         Inventory service, Email service, Analytics consumen
```

### 10. ¿Microservicios vs Monolito?
```
Monolito:
- Una aplicación grande
- Deploy todo junto
- Más simple inicialmente
- Escala verticalmente

Microservicios:
- Servicios pequeños e independientes
- Deploy independiente
- Más complejo, más flexible
- Escala horizontalmente
- Cada servicio puede usar diferente tech stack

impact.com probablemente usa microservicios para manejar
diferentes productos (Performance, Creator, Advocate).
```

---

# BEHAVIORAL QUESTIONS - RESPUESTAS STAR

## "Tell me about yourself" (2 minutos)

```
"I'm Andres, a Backend Software Engineer based here in Victoria, BC.

I have extensive experience building scalable applications using Java 
Spring Framework, JavaScript, and cloud technologies like AWS.

For the past 10+ years at ASZENDER, I've designed and developed 
enterprise solutions - building REST APIs, implementing microservices 
architectures, and working with technologies like Kafka for event-driven 
systems and Jenkins for CI/CD pipelines.

Most recently, I completed a capstone project at Dyspatch where I built 
GraphQL APIs and worked with microservices using gRPC. It was one of 
the top-evaluated projects at Camosun College.

I'm particularly excited about impact.com because you work with 
world-class brands like Walmart and Shopify, building the partnership 
marketing platform. The opportunity to work on the Core team, owning 
features from idea to production, aligns perfectly with how I like to work.

I'm a strong collaborator who thrives in agile environments, and I'm 
ready to contribute to building highly scalable features."
```

## "Why impact.com?"

```
"Three main reasons:

First, the PRODUCT - partnership marketing is fascinating. The idea that 
you're powering relationships between brands and affiliates, influencers, 
and advocates is compelling. You're not just building software, you're 
enabling business growth.

Second, the SCALE - you work with brands like Walmart, Uber, Shopify. 
Building features that handle that scale is exactly the kind of challenge 
I'm looking for.

Third, the TEAM culture - I read that you've taken companies from startup 
to billion-dollar unicorns. I want to learn from that experience and 
contribute to that trajectory."
```

## "Tell me about a challenging project"

```
SITUATION: At ASZENDER, we had a client with 50,000+ users needing 
single sign-on across multiple education platforms.

TASK: I needed to design and implement an SSO solution that could handle 
high concurrent logins during peak times (start of semester).

ACTION: I architected a solution using OAuth2/OIDC with JWT tokens. 
I implemented caching with Redis to reduce database load, and used 
message queues to handle async operations. I also set up comprehensive 
monitoring to catch issues early.

RESULT: The system handled 10x the expected load during peak registration. 
Zero downtime during the first semester launch. The client renewed their 
contract for 3 more years.
```

## "How do you handle disagreements with team members?"

```
"I see disagreements as opportunities to find the best solution.

Recently, a colleague and I disagreed on whether to use REST or GraphQL 
for a new API. 

Instead of pushing my opinion, I suggested we each write a brief doc 
outlining pros and cons for our specific use case. We then discussed 
it together and realized GraphQL made more sense for the frontend's 
needs, even though I initially preferred REST.

The key is to focus on the PROBLEM, not on being right. Data and 
discussion usually lead to the best outcome."
```

---

# PREGUNTAS PARA HACERLES A ELLOS

1. "What does a typical day look like for someone on the Core team?"

2. "What are the biggest technical challenges the team is currently facing?"

3. "How do you balance building new features vs maintaining existing systems?"

4. "What does the onboarding process look like for new engineers?"

5. "What opportunities are there for learning and growth?"

---

# CHECKLIST FINAL

## Técnico
- [ ] Puedo explicar Dependency Injection con ejemplo
- [ ] Puedo crear REST API con Spring Boot
- [ ] Entiendo Spring Security y JWT
- [ ] Sé qué es Kafka y cuándo usarlo
- [ ] Puedo escribir queries SQL (JOIN, GROUP BY)
- [ ] Entiendo Vue y React basics
- [ ] Puedo explicar microservicios vs monolito

## Behavioral
- [ ] "Tell me about yourself" memorizado (2 min)
- [ ] Tengo 3 historias STAR listas
- [ ] Sé por qué quiero trabajar en impact.com
- [ ] Tengo 3+ preguntas para ellos

## Logístico
- [ ] Sé formato de entrevista (virtual/presencial)
- [ ] Ropa profesional lista
- [ ] Espacio ordenado y silencioso
- [ ] Cámara y micrófono probados
- [ ] Agua y papel listos
- [ ] Dormí 7-8 horas

---

# TIPS FINALES

1. **Si no sabes algo, sé honesto:** "No he trabajado directamente con eso, pero así es como lo abordaría..."

2. **Piensa en voz alta:** Los entrevistadores quieren ver tu proceso de pensamiento.

3. **Pide clarificación:** Si no entiendes una pregunta, pregunta. Es mejor que responder mal.

4. **Muestra entusiasmo:** Sonríe, muestra interés genuino en impact.com.

5. **Relájate:** Ya tienes la entrevista, les interesaste. Solo muestra quién eres.

---

# ¡TÚ PUEDES! 🚀

Tienes 7 días de preparación intensiva.
Tienes la experiencia.
Tienes la motivación.

**¡ÉXITO EN LA ENTREVISTA!**
