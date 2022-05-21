package ir.mahdiparastesh.pinsaver.data

import android.content.Context
import android.net.Uri
import android.text.TextUtils
import com.android.volley.*
import com.android.volley.toolbox.HttpHeaderParser
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import java.util.regex.Pattern
import kotlin.reflect.KClass

class Api<JSON>(
    c: Context,
    url: String,
    private val clazz: KClass<*>,
    method: Int = Method.GET,
    private val onSuccess: (json: JSON) -> Unit
) : Request<String>(method, encode(url),
    Response.ErrorListener {}) {

    init {
        setShouldCache(false)
        tag = "fetch"
        retryPolicy = DefaultRetryPolicy(
            15000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        Volley.newRequestQueue(c).add(this)
    }

    override fun getHeaders(): Map<String, String> = Headers()

    //override fun getBody(): ByteArray? = super.getBody()

    @Suppress("UNCHECKED_CAST")
    override fun deliverResponse(response: String) {
        onSuccess(Gson().fromJson(response, clazz.java) as JSON)
    }

    override fun parseNetworkResponse(response: NetworkResponse): Response<String> =
        Response.success(String(response.data), HttpHeaderParser.parseCacheHeaders(response))


    companion object {
        fun encode(uriString: String?): String? {
            if (uriString == null) return null
            if (TextUtils.isEmpty(uriString)) return uriString
            val allowedUrlCharacters = Pattern.compile(
                "([A-Za-z0-9_.~:/?#\\[\\]@!$&'()*+,;" + "=-]|%[0-9a-fA-F]{2})+"
            )
            val matcher = allowedUrlCharacters.matcher(uriString)
            var validUri: String? = null
            if (matcher.find()) validUri = matcher.group()
            if (TextUtils.isEmpty(validUri) || uriString.length == validUri!!.length)
                return uriString

            val uri = Uri.parse(uriString)
            val uriBuilder = Uri.Builder().scheme(uri.scheme).authority(uri.authority)
            for (path in uri.pathSegments) uriBuilder.appendPath(path)
            for (key in uri.queryParameterNames)
                uriBuilder.appendQueryParameter(key, uri.getQueryParameter(key))
            return uriBuilder.build().toString()
        }
    }

    @Suppress("SpellCheckingInspection")
    enum class Endpoint(val url: String) {
        // Explore popular ideas
        TOP_PINS(
            "https://www.pinterest.com/resource/SeoTier1CandidateResource/get/" +
                    "?source_url=%2Fideas%2F" +
                    "&data=%7B\"options\"%3A%7B\"bookmarks\"%3A%5B\"%1\\\$s\"%5D%7D%2C\"context\"%3A%7B%7D%7D" +
                    "&_=%2\$d"
        ),
        // Put encode(resource_response.bookmark) and TIMESTAMP in milliseconds in the arguments
        // of the next fetch...

        // Search (&rs=filter)
        SEARCH_PEOPLE("https://www.pinterest.com/search/users/?q=%s"),
        SEARCH_BOARDS("https://www.pinterest.com/search/boards/?q=%s"),
        SEARCH_PINS("https://www.pinterest.com/search/pins/?q=%s"),
        SEARCH_VIDEOS("https://www.pinterest.com/search/videos/?q=%s"),
        SEARCH_MY_PINS("https://www.pinterest.com/search/my_pins/?q=%s"),
    }

    class Headers : HashMap<String, String>()
}
