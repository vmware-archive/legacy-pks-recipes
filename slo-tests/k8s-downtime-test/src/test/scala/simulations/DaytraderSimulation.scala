package simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration._

class DaytraderSimulation extends Simulation {

  val baseUrl = System.getProperty("TARGET_URL")
  val simUsers = System.getProperty("SIM_USERS", "3").toInt
  val repeatDuration = System.getProperty("DURATION", "5").toInt

  val httpConf = http
    .baseUrl(baseUrl)
  //    .inferHtmlResources()

  val headers = Map("Accept" -> "text/html")

  val feeder = Array(
    Map("user" -> "uid:0", "password" -> "xxx"),
    Map("user" -> "uid:1", "password" -> "xxx"),
    Map("user" -> "uid:2", "password" -> "xxx"),
    Map("user" -> "uid:3", "password" -> "xxx"),
    Map("user" -> "uid:4", "password" -> "xxx"),
    Map("user" -> "uid:5", "password" -> "xxx"),
    Map("user" -> "uid:6", "password" -> "xxx"),
    Map("user" -> "uid:7", "password" -> "xxx"),
    Map("user" -> "uid:8", "password" -> "xxx"),
    Map("user" -> "uid:9", "password" -> "xxx"),
    Map("user" -> "uid:10", "password" -> "xxx")
  ).random


  val sampleUserFlow = during(repeatDuration minutes) {
    exec(http("daytrader home page")
      .get("/daytrader/").check(status.in(200, 304))
    )
      .pause(1 seconds)
      .exec(
        http("loginpage")
          .get("/daytrader/app").check(substring("Log in"))
          .headers(headers)
      )
      .pause(1 second)
      .feed(feeder)
      .exec(
        http("perform login")
          .post("/daytrader/app")
          .formParam("uid", "${user}")
          .formParam("passwd", "${password}")
          .formParam("action", "login")
          .headers(headers)
          .check(substring("Welcome"))
      )
      .pause(1 seconds)
      .exec(
        http("Account Summary")
          .get("/daytrader/app?action=account")
          .headers(headers)
          .check(substring("Account Information"))
      )
      .pause(1 second)
      .exec(
        http("Home")
          .get("/daytrader/app?action=home")
          .headers(headers)
          .check(substring("Welcome"))
      )
      .pause(1 second)
      .exec(
        http("Portfolio Page")
          .get("/daytrader/app?action=portfolio")
          .headers(headers)
          .check(substring("of Holdings"))
      )
      .pause(1 second)
      .exec(
        http("Quotes And Trades")
          .get("/daytrader/app?action=quotes&symbols=s:0,s:1,s:2,s:3,s:4")
          .headers(headers)
          .check(substring("Quotes"))
      )
      .pause(1 second)
      .exec(http("Logout")
        .get("/daytrader/app?action=logout")
        .headers(headers)
        .check(substring("Log in"))
      )
      .pause(1 second)
  }


  val scn = scenario("Day Trader App")
    .exec(sampleUserFlow)

  setUp(scn.inject(atOnceUsers(simUsers)).protocols(httpConf))
}
