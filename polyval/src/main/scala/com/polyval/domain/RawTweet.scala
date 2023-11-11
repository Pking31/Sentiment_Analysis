package com.polyval.domain

final case class RawTweet(
    id: Option[Int],
    text: Option[String],
    favorited: Option[Boolean],
    favoriteCount: Option[Int],
    replyToSN: Option[String],
    created: Option[String],
    truncated: Option[Boolean],
    replyToSID: Option[String],
    replyToUID: Option[String],
    statusSource: Option[String],
    screenName: Option[String],
    retweetCount: Option[Int],
    isRetweet: Option[Boolean],
    retweeted: Option[Boolean],
    longitude: Option[String],
    latitude: Option[String]
)

object RawTweet {
  import io.circe.*
  import io.circe.generic.semiauto.*

  implicit val RawTweetDecoder: Decoder[RawTweet] = deriveDecoder[RawTweet]

  implicit val RawTweetEncoder: Encoder[RawTweet] = deriveEncoder[RawTweet]

  import com.polyval.syntax.RowDecoder
  import com.polyval.syntax.row.*

  implicit val RowTweetDecoder: RowDecoder[RawTweet] = RowDecoder.instance { row =>
    Some(
      RawTweet(
        row.fromInt(0),
        row.fromString(1),
        row.fromBoolean(2),
        row.fromInt(3),
        row.fromString(4),
        row.fromString(5),
        row.fromBoolean(6),
        row.fromString(7),
        row.fromString(8),
        row.fromString(9),
        row.fromString(10),
        row.fromInt(11),
        row.fromBoolean(12),
        row.fromBoolean(13),
        row.fromString(14),
        row.fromString(15)
      )
    )
  }
}
