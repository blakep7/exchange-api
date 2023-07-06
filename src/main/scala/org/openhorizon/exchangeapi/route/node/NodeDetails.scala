package org.openhorizon.exchangeapi.route.node

import org.openhorizon.exchangeapi.StrConstants
import org.openhorizon.exchangeapi.table.{NodeHeartbeatIntervals, OneProperty, OneService, OneUserInputService, RegService}

case class NodeDetails(arch: Option[String] = None,
                       connectivity: Option[Map[String, Boolean]] = None,
                       constraints: Option[List[String]] = None,
                       errors: Option[List[Any]] = None,
                       heartbeatIntervals: Option[NodeHeartbeatIntervals] = None,
                       id: String = "",
                       lastHeartbeat: Option[String] = None,
                       lastUpdatedNode: String = "",
                       lastUpdatedNodeError: Option[String] = None,
                       lastUpdatedNodePolicy: Option[String] = None,
                       lastUpdatedNodeStatus: Option[String] = None,
                       msgEndPoint: Option[String] = None,
                       name: Option[String] = None,
                       nodeType: String = "",
                       owner: String = "",
                       orgid: String = "",
                       pattern: Option[String] = None,
                       properties: Option[List[OneProperty]] = None,
                       publicKey: Option[String] = None,
                       registeredServices: Option[List[RegService]] = None,
                       runningServices: Option[String] = None,
                       services: Option[List[OneService]] = None,
                       softwareVersions: Option[Map[String, String]] = None,
                       token: String = StrConstants.hiddenPw,
                       userInput: Option[List[OneUserInputService]] = None,
                       ha_group: Option[String] = None,
                       clusterNamespace: Option[String] = None)