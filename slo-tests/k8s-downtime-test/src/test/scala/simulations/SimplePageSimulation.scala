package simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration._

class SimplePageSimulation extends Simulation {

  val baseUrl = System.getProperty("TARGET_URL")
  val simUsers = System.getProperty("SIM_USERS", "3").toInt
  val repeatDuration = System.getProperty("DURATION", "5").toInt

  val httpConf = http
    .baseUrl(baseUrl)


  val sampleUserFlow = during(repeatDuration minutes) {
    exec(http("Ping endpoint")
      .get("/sample-war-proj/ping")
      .check(status.in(200))
      .check(substring("pong"))
    )
      .pause(1 seconds, 2 seconds)
  }


  val scn = scenario("Sample War Proj App")
    .exec(sampleUserFlow)

  setUp(scn.inject(atOnceUsers(simUsers)).protocols(httpConf))
}
