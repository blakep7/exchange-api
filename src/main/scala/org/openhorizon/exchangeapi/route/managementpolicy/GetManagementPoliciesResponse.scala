package org.openhorizon.exchangeapi.route.managementpolicy

import org.openhorizon.exchangeapi.table.managementpolicy.ManagementPolicy

final case class GetManagementPoliciesResponse(managementPolicy: Map[String, ManagementPolicy],
                                               lastIndex: Int)
