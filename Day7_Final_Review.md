# DAY 7 - FINAL REVIEW & INTERVIEW PREPARATION
## Impact.com Senior Full-Stack Engineer Interview
### Thursday, January 15, 2026

---

# SCHEDULE FOR THURSDAY

| Time | Block | Duration | Focus |
|------|-------|----------|-------|
| 6:00 AM | Wake up | - | Breakfast, fresh mind |
| 7:00 AM | Block 1 | 2h | Quick Review - All Key Concepts |
| 9:00 AM | Break | 30min | Walk, relax |
| 9:30 AM | Block 2 | 2h | Behavioral Questions - STAR Method |
| 11:30 AM | Lunch | 1h | Eat well, relax |
| 12:30 PM | Block 3 | 1h | Company Research - impact.com |
| 1:30 PM | Block 4 | 1h | Final System Design Review |
| 2:30 PM | Block 5 | 1h | Mock Interview Practice (out loud) |
| 4:00 PM | Done | - | Light activity, NO more studying |
| 9:00 PM | Evening | - | Prepare clothes, setup, early sleep |
| 10:00 PM | Sleep | - | 8 hours before interview |

---

# BLOCK 1: QUICK REVIEW - ALL KEY CONCEPTS (2 hours)

## 1. Spring Boot - Key Points

```
Dependency Injection:
- @Autowired, @Component, @Service, @Repository
- Constructor injection (recommended)
- @Qualifier for multiple beans

Spring Security:
- SecurityFilterChain
- JWT authentication
- @PreAuthorize for method security

Spring Data JPA:
- JpaRepository interface
- Custom queries with @Query
- Pagination with Pageable
```

## 2. Kafka - Key Points

```
Core Concepts:
- Producer → Topic → Consumer
- Topic has Partitions (parallel processing)
- Consumer Group shares partitions
- Offset tracks position

Ordering:
- Guaranteed ONLY within partition
- Same key → same partition

Spring Kafka:
- KafkaTemplate for producing
- @KafkaListener for consuming
- Acknowledgment for manual commit
```

## 3. SQL - Key Points

```
JOINs:
- INNER: Only matching rows
- LEFT: All left + matching right
- RIGHT: All right + matching left
- FULL OUTER: All from both

Indexes:
- B-Tree (default): equality, range, sorting
- Composite: order matters (left-to-right)
- Use on: WHERE, JOIN, ORDER BY columns

Transactions:
- ACID: Atomicity, Consistency, Isolation, Durability
- Isolation levels: READ COMMITTED (default), REPEATABLE READ, SERIALIZABLE
- @Transactional in Spring
```

## 4. System Design - Key Points

```
Event-Driven Architecture:
- Loose coupling
- Async processing
- Event Sourcing: events as source of truth
- CQRS: separate read/write models
- Saga: distributed transactions

Microservices:
- Independent services
- Own database per service
- API Gateway
- Service discovery
```

## 5. Docker & CI/CD - Key Points

```
Docker:
- Dockerfile: FROM, COPY, RUN, EXPOSE, ENTRYPOINT
- Multi-stage builds for optimization
- docker-compose for multiple services

Jenkins:
- Declarative Pipeline
- Stages: Checkout → Build → Test → Deploy
- Parallel execution
- Credentials management
```

## 6. AWS - Key Points

```
EC2: Virtual servers (t3.micro, m5.large)
RDS: Managed database (PostgreSQL, MySQL)
S3: Object storage (buckets, presigned URLs)
Lambda: Serverless functions (event triggers)
```

## 7. React - Key Points

```
Hooks:
- useState: local state
- useEffect: side effects
- useContext: global state
- useMemo/useCallback: optimization

State Management:
- Props for parent→child
- Context for global
- Redux for complex state
```

---

# BLOCK 2: BEHAVIORAL QUESTIONS - STAR METHOD (2 hours)

## What is STAR Method?

```
S - SITUATION: Set the context
T - TASK: Describe your responsibility
A - ACTION: Explain what YOU did (use "I")
R - RESULT: Share the outcome (quantify if possible)
```

## Key Behavioral Questions for impact.com

### Q1: "Tell me about yourself"

**Answer (2 minutes max):**

```
"I'm a Full-Stack Developer with over 7 years of experience, 
specializing in backend development with Java, Spring Boot, 
and Node.js.

Currently, I'm Co-Founder and Cloud Engineer at ASZENDER, 
where I've built identity and access management solutions 
serving over 50,000 users across education and enterprise clients.

I recently completed my Computer Systems Technology diploma 
at Camosun College here in Victoria, where I achieved Dean's 
Honour Roll and led a capstone project at Dyspatch. There, 
I built microservices using GraphQL, TypeScript, and Node.js 
that received top evaluation.

I'm excited about this opportunity at impact.com because of 
your focus on partnership automation and the chance to work 
with technologies like Spring Boot, Kafka, and React at scale. 
Your mission to help businesses grow through partnerships 
really resonates with me."
```

---

### Q2: "Tell me about a challenging technical problem you solved"

**STAR Answer:**

```
SITUATION:
"At ASZENDER, we had a critical performance issue with our 
identity management system. User authentication was taking 
over 8 seconds during peak hours, which was causing timeouts 
and frustrating our 50,000+ users."

TASK:
"I was responsible for identifying the bottleneck and 
implementing a solution while ensuring zero downtime for 
our education clients who depended on the system daily."

ACTION:
"First, I profiled the application and discovered that 
each authentication request was making 12 separate database 
queries. I implemented several optimizations:

1. I redesigned the database queries using JOINs to reduce 
   the 12 queries to just 2
2. I added Redis caching for frequently accessed user 
   permissions with a 5-minute TTL
3. I implemented connection pooling with HikariCP
4. I added database indexes on the most queried columns

I also set up monitoring with metrics to track response 
times going forward."

RESULT:
"Authentication time dropped from 8 seconds to under 200 
milliseconds - a 97% improvement. User complaints about 
timeouts dropped to zero, and we were able to handle 3x 
more concurrent users without adding infrastructure."
```

---

### Q3: "Describe a time you had to learn a new technology quickly"

**STAR Answer:**

```
SITUATION:
"During my capstone project at Dyspatch, I was assigned 
to build a feature using GraphQL and a microservices 
architecture. I had strong REST API experience but had 
never worked with GraphQL in production."

TASK:
"I needed to become proficient in GraphQL within 2 weeks 
to design and implement a production-ready API that would 
integrate with their existing email template platform."

ACTION:
"I created an intensive learning plan:

1. First week: I completed the official GraphQL documentation, 
   took an online course, and built 3 small practice projects
2. I studied Dyspatch's existing codebase to understand 
   their patterns and conventions
3. I paired with senior developers to review my initial 
   schema designs
4. I implemented the feature incrementally, getting feedback 
   at each stage

I also documented everything I learned for future team members."

RESULT:
"I delivered the feature on time and it passed all code 
reviews on the first submission. The project received top 
evaluation from Dyspatch. More importantly, I now use 
GraphQL confidently and have implemented it in other projects."
```

---

### Q4: "Tell me about a time you disagreed with a team member"

**STAR Answer:**

```
SITUATION:
"At ASZENDER, a colleague wanted to implement a complex 
microservices architecture for a new feature. I believed 
a simpler approach would be better for our small team 
and timeline."

TASK:
"I needed to express my concerns professionally while 
respecting my colleague's expertise and finding the best 
solution for the project."

ACTION:
"Instead of arguing, I suggested we both document our 
approaches with pros and cons. I created a comparison 
showing:

1. Development time estimates for each approach
2. Maintenance complexity
3. Team expertise required
4. Scalability needs based on actual user projections

I focused on data and project requirements, not personal 
opinions. We scheduled a meeting to discuss both options 
with the full team."

RESULT:
"After reviewing the analysis, we agreed on a hybrid 
approach - starting with a modular monolith that could 
be split into microservices later if needed. The feature 
launched on time, and my colleague appreciated the 
collaborative approach. We've used this decision-making 
framework for subsequent architectural decisions."
```

---

### Q5: "Describe a time you failed or made a mistake"

**STAR Answer:**

```
SITUATION:
"Early in my career at ASZENDER, I pushed a database 
migration to production without proper testing. The 
migration had a bug that caused some user permissions 
to be incorrectly assigned."

TASK:
"I needed to fix the issue immediately, restore correct 
permissions, and ensure this type of mistake never 
happened again."

ACTION:
"First, I immediately notified my team and stakeholders 
about the issue. I wrote a rollback script and carefully 
restored the affected permissions within 2 hours.

Then I took responsibility for improving our process:
1. I implemented a staging environment that mirrors production
2. I created a pre-deployment checklist for all migrations
3. I set up automated tests for permission changes
4. I documented the incident and shared lessons learned"

RESULT:
"We recovered fully within 2 hours with no lasting impact 
on users. The new processes I implemented have prevented 
similar issues for over 3 years. This experience taught 
me the importance of thorough testing and having rollback 
plans, which I apply to every deployment now."
```

---

### Q6: "How do you handle tight deadlines or pressure?"

**STAR Answer:**

```
SITUATION:
"During my final semester at Camosun College, I had to 
balance my capstone project at Dyspatch, final exams, 
and maintaining my work at ASZENDER - all with strict 
deadlines."

TASK:
"I needed to deliver quality work on all fronts without 
burning out or compromising on any commitment."

ACTION:
"I implemented a structured approach:

1. I broke each project into small, daily tasks
2. I used time-blocking: mornings for deep coding work, 
   afternoons for meetings and reviews
3. I communicated proactively with all stakeholders about 
   my availability and progress
4. I identified tasks that could be delegated or deferred
5. I maintained healthy habits: regular sleep, exercise, 
   and breaks"

RESULT:
"I delivered the Dyspatch capstone on time with top evaluation, 
achieved Dean's Honour Roll, and maintained my responsibilities 
at ASZENDER. I learned that planning and communication are 
more effective than just working longer hours."
```

---

### Q7: "Why do you want to work at impact.com?"

**Answer:**

```
"Three main reasons:

First, I'm genuinely excited about partnership automation. 
Having built B2B solutions at ASZENDER, I understand how 
partnerships can drive business growth. impact.com's platform 
helps companies scale their partnership programs, which is 
a meaningful problem to solve.

Second, the technology stack is exactly what I want to work 
with. Spring Boot, Kafka, React - these are technologies I'm 
experienced in and want to go deeper with. I'm particularly 
interested in your event-driven architecture and how you 
handle real-time attribution at scale.

Third, I value the opportunity to work with a global team 
on products used by major brands like Walmart and Uber. 
The scale of impact.com's platform would challenge me to 
grow as an engineer while contributing to a company that's 
clearly a leader in its space."
```

---

### Q8: "Where do you see yourself in 5 years?"

**Answer:**

```
"In 5 years, I see myself as a senior technical leader - 
possibly a Staff Engineer or Tech Lead - who combines deep 
technical expertise with the ability to mentor others and 
drive architectural decisions.

I want to become an expert in distributed systems and 
event-driven architecture. I'm also interested in eventually 
contributing to engineering strategy and helping build 
high-performing teams.

At impact.com specifically, I'd love to grow from contributing 
individual features to owning larger system components and 
eventually helping shape the technical direction of the 
platform."
```

---

### Q9: "Tell me about a project you're most proud of"

**STAR Answer:**

```
SITUATION:
"At ASZENDER, we needed to build an identity and access 
management platform from scratch to serve educational 
institutions across Latin America."

TASK:
"As Co-Founder and lead developer, I was responsible for 
designing and implementing the entire IAM system that 
would need to scale to serve tens of thousands of users."

ACTION:
"I made several key technical decisions:

1. Chose a microservices architecture with Node.js and 
   NestJS for flexibility and performance
2. Implemented OAuth 2.0 and SAML for enterprise SSO
3. Integrated with Microsoft 365 and Google Workspace APIs
4. Built a role-based access control system with 
   hierarchical permissions
5. Designed for multi-tenancy to serve multiple institutions
6. Implemented comprehensive audit logging for compliance"

RESULT:
"The platform now serves over 50,000 users across multiple 
educational institutions. We achieved 99.9% uptime and handle 
authentication in under 200ms. The system has been running 
for over 10 years and continues to grow. This project taught 
me end-to-end system design and the importance of building 
for scale from day one."
```

---

### Q10: "Do you have any questions for us?"

**Questions to Ask (pick 3-4):**

```
About the Role:
1. "What would success look like in this role after 90 days? 
    After the first year?"

2. "What's the most challenging technical problem the team 
    is working on right now?"

3. "How is the team structured? How many engineers, and how 
    do you collaborate with product and design?"

About Technology:
4. "Can you tell me more about your event-driven architecture? 
    What message broker do you use and how do you handle 
    event consistency?"

5. "What does the CI/CD pipeline look like? How often do 
    you deploy to production?"

6. "How do you handle technical debt? Is there dedicated 
    time for refactoring?"

About Culture:
7. "What do you enjoy most about working at impact.com?"

8. "How does the team approach code reviews and knowledge 
    sharing?"

9. "What opportunities are there for learning and 
    professional development?"

About the Future:
10. "Where do you see the product heading in the next 
     1-2 years?"
```

---

## Additional Behavioral Questions to Prepare

| Question | Key Points to Cover |
|----------|-------------------|
| "How do you prioritize tasks?" | Use frameworks, communicate, focus on impact |
| "Describe your ideal work environment" | Collaborative, learning-focused, autonomous |
| "How do you stay updated with technology?" | Courses, docs, side projects, conferences |
| "Tell me about leading a project" | Planning, delegation, communication, delivery |
| "How do you handle feedback?" | Open to it, use it to improve, give examples |

---

# BLOCK 3: COMPANY RESEARCH - IMPACT.COM (1 hour)

## About impact.com

```
Founded: 2008 (as Impact Radius)
Headquarters: Santa Barbara, CA
Employees: 1,000+
Funding: Series D, $150M+
Valuation: Unicorn status ($1B+)

Mission: "Transform the way enterprises manage and optimize 
all types of partnerships"
```

## What They Do

```
Partnership Automation Platform:
- Affiliate marketing management
- Influencer partnerships
- B2B partnerships
- Mobile partnerships

Key Features:
- Partner discovery and recruitment
- Contract management
- Tracking and attribution
- Payment automation
- Analytics and reporting
```

## Major Clients

```
- Walmart
- Uber
- Shopify
- Lenovo
- Fanatics
- Levi's
- TripAdvisor
```

## Technology Stack (from job posting)

```
Backend:
- Java, Spring Boot
- Kafka, RabbitMQ
- MySQL, PostgreSQL
- Microservices architecture

Frontend:
- React, TypeScript
- Redux

Infrastructure:
- AWS
- Kubernetes
- Jenkins
- Docker
```

## Recent News & Achievements

```
- Named a Leader in Partnership Management by Forrester
- Processes billions of dollars in partnership revenue
- Global presence: offices in US, UK, South Africa, Australia
- Rapid growth: hiring across all engineering teams
```

## Victoria Office

```
- Growing engineering hub
- Focus on core platform development
- Collaborative, startup-like culture within larger company
- Competitive Canadian salaries ($125K-$145K)
```

## Why impact.com? (For your answers)

```
1. Product Impact: Help businesses grow through partnerships
2. Scale: Process billions in transactions, serve Fortune 500
3. Technology: Modern stack (Spring Boot, Kafka, React)
4. Growth: Fast-growing company with career opportunities
5. Location: Victoria office, hybrid work
```

---

# BLOCK 4: FINAL SYSTEM DESIGN REVIEW (1 hour)

## System Design Question Framework

```
1. CLARIFY REQUIREMENTS (2-3 min)
   - Functional requirements
   - Non-functional requirements
   - Scale estimates

2. HIGH-LEVEL DESIGN (5-7 min)
   - Draw main components
   - Show data flow
   - Identify APIs

3. DEEP DIVE (10-15 min)
   - Database schema
   - API design
   - Scaling strategy

4. WRAP UP (2-3 min)
   - Trade-offs
   - Future improvements
```

## Practice Question: Design a Partner Attribution System

```
Requirements:
- Track when a customer clicks a partner's link
- Attribute sales to the correct partner
- Handle millions of clicks per day
- Real-time reporting for partners

High-Level Design:

┌──────────┐     ┌──────────────┐     ┌──────────────┐
│  Click   │────▶│   API        │────▶│   Kafka      │
│  (User)  │     │   Gateway    │     │   (Events)   │
└──────────┘     └──────────────┘     └──────────────┘
                                             │
                        ┌────────────────────┼────────────────────┐
                        ▼                    ▼                    ▼
                 ┌──────────────┐    ┌──────────────┐    ┌──────────────┐
                 │   Click      │    │   Order      │    │   Analytics  │
                 │   Service    │    │   Service    │    │   Service    │
                 └──────────────┘    └──────────────┘    └──────────────┘
                        │                    │                    │
                        ▼                    ▼                    ▼
                 ┌──────────────┐    ┌──────────────┐    ┌──────────────┐
                 │   Redis      │    │   MySQL      │    │   ClickHouse │
                 │   (Cache)    │    │   (Orders)   │    │   (Analytics)│
                 └──────────────┘    └──────────────┘    └──────────────┘

Key Components:
1. Click tracking with unique click ID
2. Cookie/fingerprint for attribution window
3. Kafka for event streaming
4. Redis for fast click lookups
5. ClickHouse for real-time analytics
```

---

# INTERVIEW DAY CHECKLIST

## Thursday Evening

- [ ] Lay out professional clothes
- [ ] Test camera and microphone
- [ ] Charge laptop
- [ ] Prepare water and paper for notes
- [ ] Set 2 alarms for 8:00 AM
- [ ] Light dinner, no heavy food
- [ ] No caffeine after 2 PM
- [ ] Sleep by 10:00 PM

## Friday Morning

- [ ] 8:00 AM - Wake up, shower, breakfast
- [ ] 9:00 AM - Light review (DO NOT cram new material)
- [ ] 9:30 AM - Review your STAR answers out loud
- [ ] 9:45 AM - Setup: camera, mic, water, paper
- [ ] 9:55 AM - Join meeting 5 minutes early
- [ ] 10:00 AM - INTERVIEW TIME!

## During Interview

- [ ] SMILE - First impressions matter
- [ ] LISTEN - Don't interrupt, ask clarifying questions
- [ ] THINK - Take 5-10 seconds before answering
- [ ] BREATHE - Pause if nervous
- [ ] BE HONEST - Say "I don't know" if needed, then explain how you'd find out

## Key Phrases to Use

```
Starting an answer:
- "That's a great question. Let me think about that..."
- "In my experience at ASZENDER/Dyspatch..."

If you don't know:
- "I haven't worked with that specifically, but here's how I'd approach it..."
- "I'm not 100% sure, but my understanding is..."

Asking for clarification:
- "Just to make sure I understand, are you asking about...?"
- "Could you give me more context about...?"

Wrapping up:
- "Does that answer your question, or would you like me to go deeper?"
```

---

# FINAL REMINDERS

## Your Strengths (Emphasize these!)

```
✅ 7+ years backend experience
✅ Identity & Access Management expertise (rare!)
✅ Spring Boot, Node.js, TypeScript
✅ Microservices & GraphQL (Dyspatch capstone)
✅ Dean's Honour Roll at Camosun
✅ Real production systems serving 50,000+ users
✅ Local to Victoria (no relocation needed)
```

## Your Story

```
"I'm a backend-focused full-stack developer who has spent 
10+ years building identity management systems. I'm now 
looking to apply that experience at a larger scale company 
like impact.com, where I can work on challenging problems 
with a talented team."
```

## Confidence Boosters

```
- You have MORE experience than many candidates
- You've built REAL systems used by REAL users
- You have UNIQUE IAM expertise
- You are LOCAL to Victoria
- You've prepared thoroughly for THIS interview
- You belong in this role!
```

---

# YOU'VE GOT THIS! 🎯

Remember:
- They already liked your resume enough to interview you
- Be yourself, be curious, be confident
- It's a conversation, not an interrogation
- Even if it doesn't work out, it's great practice

**Good luck, Andres!**
