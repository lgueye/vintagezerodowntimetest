import java.time.Instant

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration._
import scala.util.Random

class CreateMeasurementFacts extends Simulation {

  val baseUrl = System.getProperty("vintagezerodowntime.engine.server.api.url")
  val usersPerSec = System.getProperty("gatling.users.per.second").toDouble
  val injectionDurationInMinutes = System.getProperty("gatling.scenario.duration.in.minutes").toDouble

  def timestampRef() = Instant.now.toEpochMilli

  object HeartRateFact {

    def valueRef() = Random.nextInt(220 - 80) + 80
    val post = exec(http("post_hart_rates")
      .post("/api/providers/medrate/facts")
      .body(StringBody(session => s"""{ "measurement": "heart_rate", "provider": "medrate", "deviceBusinessId": "D-8563461", "value": "${valueRef()}", "timestamp": "${timestampRef()}" }""")))

  }

  object RespirationRateFact {

    def valueRef() = Random.nextInt(20 - 12) + 12
    val post = exec(http("post_hart_rates")
      .post("/api/providers/medrate/facts")
      .body(StringBody(session => s"""{ "measurement": "respiration_rate", "provider": "medrate", "deviceBusinessId": "D-8563461", "value": "${valueRef()}", "timestamp": "${timestampRef()}" }""")))

  }

  val httpConf = http
    .baseURL(baseUrl)
    .header("Content-Type", "application/json")

  val postHeartRates = scenario("post_hart_rates").exec(HeartRateFact.post)
  val postRespirationRates = scenario("post_respiration_rates").exec(RespirationRateFact.post)

  setUp(
    postHeartRates.inject(constantUsersPerSec(usersPerSec) during(injectionDurationInMinutes minutes)),
    postRespirationRates.inject(constantUsersPerSec(usersPerSec) during(injectionDurationInMinutes minutes))
  ).protocols(httpConf)
}
