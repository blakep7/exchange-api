package org.openhorizon.exchangeapi.route.agreementbot

import org.openhorizon.exchangeapi.table.agreementbot.AAService
import org.openhorizon.exchangeapi.table.agreementbot.agreement.AgbotAgreementRow
import org.openhorizon.exchangeapi.utility.ApiTime

/** Input format for PUT /orgs/{orgid}/agbots/{id}/agreements/<agreement-id> */
final case class PutAgbotAgreementRequest(service: AAService, state: String) {
  require(service!=null && state!=null)
  def getAnyProblem: Option[String] = None

  def toAgbotAgreementRow(agbotId: String, agrId: String): AgbotAgreementRow = {
    AgbotAgreementRow(agrId, agbotId, service.orgid, service.pattern, service.url, state, ApiTime.nowUTC, "")
  }
}
