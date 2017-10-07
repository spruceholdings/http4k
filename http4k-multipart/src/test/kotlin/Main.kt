
import org.http4k.client.ApacheClient
import org.http4k.core.ContentType
import org.http4k.core.Method
import org.http4k.core.MultipartEntity
import org.http4k.core.MultipartFormBody
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.core.then
import org.http4k.core.with
import org.http4k.filter.ServerFilters
import org.http4k.lens.Header
import org.http4k.server.SunHttp
import org.http4k.server.asServer


fun main(args: Array<String>) {

    val form = MultipartFormBody(
        MultipartEntity.Field("field", "value"),
        MultipartEntity.File("file", "file.yxy", ContentType.TEXT_HTML, "some html".byteInputStream())
    )

    val s = ServerFilters.CatchAll().then({ r: Request ->
        val a = MultipartFormBody.from(r)
        println("received the same? ${a.fields("field")}")
        println("received the same? ${a.files("file")}")

        Response(OK).body(r.toString())
    }).asServer(SunHttp(8000)).start()


//    val req = Request(Method.POST, "http://localhost:8000/bob")
    val req = Request(Method.POST, "http://httpbin.org/post")
        .with(Header.Common.CONTENT_TYPE of ContentType.MultipartFormWithBoundary(form.boundary))
        .body(form)

    println(ApacheClient()(req))

    s.stop()
}