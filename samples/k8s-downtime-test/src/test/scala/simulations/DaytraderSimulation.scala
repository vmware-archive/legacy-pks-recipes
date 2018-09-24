package simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration._

class DaytraderSimulation extends Simulation {

  val baseUrl = System.getProperty("TARGET_URL")
  val sim_users = 3

  val httpConf = http
    .baseUrl(baseUrl)
//    .inferHtmlResources()

  val headers = Map("Accept" -> "text/html")

  val sampleUserFlow = during(5 minutes) {
    exec(http("daytrader home page")
      .get("/daytrader/").check(status.in(200, 304))
    ).pause(1 seconds)
      .exec(
        http("loginpage")
          .get("/daytrader/app")
          .headers(headers)
      ).pause(1 second)
      .exec(
        http("perform login")
          .post("/daytrader/app")
          .formParam("uid", "uid:0")
          .formParam("passwd", "xxx")
          .formParam("action", "login")
          .headers(headers)
      ).pause(1 seconds)
      .exec(
        http("Account Summary")
          .get("/daytrader/app?action=account")
          .headers(headers)
      ).pause(1 second)
      .exec(
        http("Portfolio Page")
          .get("/daytrader/app?action=portfolio")
          .headers(headers)
      ).pause(1 second)
      .exec(http("Logout")
        .get("/daytrader/app?action=logout")
        .headers(headers)
      ).pause(1 second)
  }


  val scn = scenario("Day Trader App")
    .exec(sampleUserFlow)

  setUp(scn.inject(atOnceUsers(sim_users)).protocols(httpConf))
}
