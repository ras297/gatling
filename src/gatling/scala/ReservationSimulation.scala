import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._
import scala.util.Random

class ReservationSimulation extends Simulation {

  private val tps: Double =
    sys.props.get("TPS").map(_.toDouble).getOrElse(1000)

  private val durationSeconds: Int =
    sys.props.get("DURATION").map(_.toInt).getOrElse(900)

  private val httpProtocol = http
    .baseUrl("http://localhost:8080")
    .contentTypeHeader("application/json")
    .acceptHeader("application/json")

  private val feeder = Iterator.continually {
	Map("holderId" -> (Random.nextInt(999) + 1))
  }

  private val scn = scenario("Reserve Slot")
    .feed(feeder)
    .exec(
      http("POST /api/reservations")
        .post("/api/reservations")
        .body(StringBody { session =>
          val holderId = session("holderId").as[Int]
          s"""{ "holderId": $holderId }"""
        })
        .asJson
        .check(status.is(201))
    )

  setUp(
    scn.inject(
      constantUsersPerSec(tps)
        .during(durationSeconds.seconds)
        .randomized
    )
  ).protocols(httpProtocol)
}
