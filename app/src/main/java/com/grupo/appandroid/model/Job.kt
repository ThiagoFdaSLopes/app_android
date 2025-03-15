// Location.kt
data class Location(
    val display_name: String
)

data class Company(
    val display_name: String
)

// Category.kt
data class Category(
    val label: String,
    val tag: String
)

// JobResponse.kt
data class JobResponse(
    val count: Int,
    val results: List<Job>?,
    val next: String?,
    val previous: String?
)

data class Job(
    val id: String,
    val title: String,
    val description: String,
    val company: Company,
    val location: Location,
    val contract_time: String?
)