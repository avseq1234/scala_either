package com.scala.either.demo

import java.net.URL
import scala.io.Source

/**
  * Created by Honda on 2017/11/9.
  */
object EitherDemo {

  def main(args: Array[String]): Unit = {

    //
    // Use Pattern Match to get Left and Right
    //

    val result = getContent(new URL("http://www.google.com"))

    result match
      {
      case Left(msg) => println(msg)
      case Right(src) => src.getLines().foreach(println)
    }

    println("==============================")

    //
    // Change Right Part Iterator[String]
    //
    val content: Either[String, Iterator[String]] =
      getContent(new URL("http://www.google.com")).right.map(_.getLines())

    content match
    {
      case Left(msg) => println(msg)
      case Right(src) => src.foreach(println)
    }

    println("==============================")

    //
    // It's not matter if the real object is Left
    //
    val content1: Either[String, Iterator[String]] =
    getContent(new URL("http://google.xxxxx.com")).right.map(_.getLines())


    content1 match
    {
      case Left(msg) => println(msg)
      case Right(src) => src.foreach(println)
    }

    println("==============================")
    val part5 = new URL("http://www.google.com")
    val part6 = new URL("http://www.google.com")

    //
    // FlatMap make it more easy
    //
    val content2 = getContent(part5).right.flatMap(a =>
      getContent(part6).right.map(b =>
        (a.getLines().size + b.getLines().size) / 2))
    content2 match
    {
      case Left(msg) => println(msg)
      case Right(size) => println(size)
    }



  }

  /*
  *
  * Left is error , Right is success
  *
  * */
  def getContent(url:URL) :Either[String,Source] =
  {
    if( url.getHost.contains("xxxxx"))
      Left("Error url")
    else
      Right(Source.fromURL(url)("Big5"))
  }


  //
  // Very strange usage
  //
  def averageLineCountWontCompile(url1: URL, url2: URL): Either[String, Int] =
    {
      for {
        source1 <- getContent(url1).right
        source2 <- getContent(url2).right
        lines1 <- Right(source1.getLines().size).right
        lines2 <- Right(source2.getLines().size).right
      } yield (lines1 + lines2) / 2
    }

}
