package example.com.totalnba.data.base

interface BaseApiModel<Model : BaseModel> {
    fun map(): Model
}
fun <Model : BaseModel> List<BaseApiModel<Model>>.map() = map { it.map() }