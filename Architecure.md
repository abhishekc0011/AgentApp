🏛 Project Structure
Presentation Layer

Fragments - UI display (AgentListFragment, AgentDetailFragment, SettingsFragment)
ViewModels - State management (AgentListViewModel, AgentDetailViewModel, SettingsViewModel)
Adapters - List rendering (AgentAdapter, PostAdapter)

Domain Layer

Models - Business entities (Agent, AgentPost)
Repository Interface - Contracts (AgentRepository)
Use Cases - Business logic (GetAgentsUseCase, SearchAgentsUseCase, etc.)

Data Layer

API Service - Retrofit interface (ApiService)
DTOs - API response objects (UserResponse, PostResponse)
Room Database - Local storage (AppDatabase, AgentEntity, PostEntity)
DAOs - Database access (AgentDao, PostDao)
Repository Implementation - Data source logic (AgentRepositoryImpl)
Worker - Background tasks (RefreshAgentsWorker)

Dependency Injection

Hilt Modules - Single provider method (UseCaseModule, NetworkModule, RepositoryModule)

💾 Caching Strategy
Three-Tier Cache
1. Memory (StateFlow)

UI state held in ViewModels
Fast access for current screen
Cleared when app dies

2. Database (Room - PRIMARY)

Persistent local SQLite
Stores agents and posts
Survives restarts
~10-100MB typical

3. Network (OkHttp - Optional)

In-memory response cache
Reduces redundant calls
