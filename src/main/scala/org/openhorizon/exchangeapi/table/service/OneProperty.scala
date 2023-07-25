package org.openhorizon.exchangeapi.table.service

final case class OneProperty(name: String,
                             `type`: Option[String],
                             value: Any) {
  override def clone() = new OneProperty(name, `type`, value)
}
