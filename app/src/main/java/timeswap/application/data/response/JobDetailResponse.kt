package timeswap.application.data.response

import timeswap.application.data.entity.Category
import timeswap.application.data.entity.Industry
import timeswap.application.data.entity.Ward

data class JobDetailResponse (
    val id: String,
    val userId: String,
    val ownerAvatarUrl: String,
    val ownerName: String,
    val ownerEmail: String,
    val ownerLocation: String,
    val title: String,
    val description: String,
    val responsibilities: String,
    val fee: Double,
    val startDate: String?,
    val dueDate: String,
    val assignedTo: String?,
    val isOwnerCompleted: Boolean,
    val isAssigneeCompleted: Boolean,
    val category: Category,
    val industry: Industry,
    val ward: Ward,
    val createdAt: String,
    val modifiedAt: String?
)