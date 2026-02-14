import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._
import scala.util.Random

class ReservationSimulation extends Simulation {

  // ---- Configurable TPS ----
  private val tps: Double =
    sys.props.get("TPS").map(_.toDouble).getOrElse(300)

  private val durationSeconds: Int =
    sys.props.get("DURATION").map(_.toInt).getOrElse(10)

  // ---- HTTP Config ----
  private val httpProtocol = http
    .baseUrl("http://localhost:8080")
    .contentTypeHeader("application/json")
    .acceptHeader("application/json")

  // ---- Feeder (Numeric holderId) ----
  private val feeder = Iterator.continually {
    //Map("holderId" -> (Random.nextInt(5_000_000) + 1))
	Map("holderId" -> (Random.nextInt(999) + 1))
  }

  // ---- Scenario ----
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

  // ---- Load Injection ----
  setUp(
    scn.inject(
      constantUsersPerSec(tps)
        .during(durationSeconds.seconds)
        .randomized
    )
  ).protocols(httpProtocol)
}
