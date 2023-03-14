import org.apache.hadoop.shaded.com.google.gson.JsonArray
import scalaj.http._
import play.api.libs.json._
import org.apache.spark.{SparkConf, SparkContext}




object Main {

  def findsmallest(arr: Array[(Int, Int)], jsonArray: Array[JsValue]): (Int,Int,Int) = {
    var smallestvalue = Integer.MAX_VALUE
    var smallestpair = (0, 0, 0)
    for (i <- 0 to arr.length - 1){
      val item = arr(i)
      val duration = (jsonArray(item._1) \ "items" \ item._2 \ "track" \ "duration_ms").as[Int]
      if (smallestvalue > duration) {
        smallestvalue = duration
        smallestpair = (item._1, item._2, i)
      }
    }
    smallestpair
  }

  def main(args: Array[String]): Unit = {
//    val conf = new SparkConf().setAppName("MyApp").setMaster("local[*]")
//    val sc = new SparkContext(conf)

    val playlistId = "5Rrf7mqN8uus2AaQQQNdc1"
    val accessToken= "BQDRzudOPgEWk0ByYNVYXr5Haz34hZLZbMGsEXAVwezzY0qsedrHowQTHRTO2WF_n7GofqtdbRMKaNm3SaUXQSMRQ_orv6QhdPShKyJt8z3sC5ADVgwhGpAPN6-j2li0hKB5O51WL2kkHlcPHSB3TuuxSf-POy_cBYdGsACljcFKlMyVHJRmVIIFo_lu5cxvk2ck"
    val limit = 50

    val jsonArray: Array[JsValue] = new Array[JsValue](10)

    for (i <- 0 to 9) {
      val offset = i * 50
      val response: HttpResponse[String] = Http(s"https://api.spotify.com/v1/playlists/$playlistId/tracks?limit=$limit&offset=$offset")
        .header("Authorization", s"Bearer $accessToken")
        .asString
      val json = Json.parse(response.body)
      jsonArray(i) = json
//      if (i == 0){
//        println(response)
//        val duration = (json \ "items" \ 0 \ "track" \ "duration_ms").as[Int]
//        println(duration)
//      }
    }

//    println(json)
//    println(response)

    //println(jsonArray(9))

//    println(duration)
    //problem 1
//    var p1map = Map[String, Int]()
    var pairs: Array[(Int, Int)] = Array.ofDim[(Int, Int)](10)
    var smallestinmap = (0, 0, 0);
//    println(p1map.size)
    pairs(0) = (0,0)
    for (i <- 0 to 9) {
      for (j <- 0 to 49) {
        val duration = (jsonArray(i) \ "items" \ j \ "track" \ "duration_ms").as[Int]
        if (i == 0 && j < 10) {
          pairs(j) = (i, j)
        } else if (i == 0 && j == 10) {
          smallestinmap = findsmallest(pairs, jsonArray)
        } else if ((jsonArray(smallestinmap._1) \ "items" \ smallestinmap._2 \ "track" \ "duration_ms").as[Int] <= duration) {
          pairs(smallestinmap._3) = (i, j)
          smallestinmap = findsmallest(pairs, jsonArray)
        }
      }
    }
    for (item <- pairs){
      println(((jsonArray(item._1)) \ "items" \ item._2 \ "track" \ "name").as[String],((jsonArray(item._1)) \ "items" \ item._2 \ "track" \ "duration_ms").as[Int])
    }
    // problem 2, unable to solve.
    val response: HttpResponse[String] = Http(s"https://api.spotify.com/v1/artists/id")
      .header("Authorization", s"Bearer $accessToken")
      .asString
  }
}