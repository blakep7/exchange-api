package org.openhorizon.exchangeapi.auth

import akka.event.LoggingAdapter
import org.openhorizon.exchangeapi.auth.Access.Access
import org.openhorizon.exchangeapi.utility.ExchMsg

import scala.util.{Failure, Success, Try}

case class IAgbot(creds: Creds) extends Identity {
  override lazy val role: String = AuthRoles.Agbot

  def authorizeTo(target: Target, access: Access)(implicit logger: LoggingAdapter): Try[Identity] = {
    // Transform any generic access into specific access
    val requiredAccess: Access =
      if (isMyOrg(target) || target.isPublic || isMultiTenantAgbot) {
        target match {
          case TUser(id) => access match { // an agbot accessing a user
            case Access.READ => Access.READ_ALL_USERS
            case Access.WRITE => Access.WRITE_ALL_USERS
            case Access.CREATE => if (Role.isSuperUser(id)) Access.CREATE_SUPERUSER else Access.CREATE_USER
            case _ => access
          }
          case TNode(_) => access match { // an agbot accessing a node
            case Access.READ => Access.READ_ALL_NODES
            case Access.WRITE => Access.WRITE_ALL_NODES
            case Access.CREATE => Access.CREATE_NODE
            case _ => access
          }
          case TAgbot(id) => access match { // an agbot accessing a agbot
            case Access.READ => if (id == creds.id) Access.READ_MYSELF else if (target.mine) Access.READ_MY_AGBOTS else Access.READ_ALL_AGBOTS
            case Access.WRITE => if (id == creds.id) Access.WRITE_MYSELF else if (target.mine) Access.WRITE_MY_AGBOTS else Access.WRITE_ALL_AGBOTS
            case Access.CREATE => Access.CREATE_AGBOT
            case _ => access
          }
          case TService(_) => access match { // an agbot accessing a service
            case Access.READ => Access.READ_ALL_SERVICES
            case Access.WRITE => Access.WRITE_ALL_SERVICES
            case Access.CREATE => Access.CREATE_SERVICES
            case _ => access
          }
          case TPattern(_) => access match { // an agbot accessing a pattern
            case Access.READ => Access.READ_ALL_PATTERNS
            case Access.WRITE => Access.WRITE_ALL_PATTERNS
            case Access.CREATE => Access.CREATE_PATTERNS
            case _ => access
          }
          case TBusiness(_) => access match { // an agbot accessing a business policy
            case Access.READ => Access.READ_ALL_BUSINESS
            case Access.WRITE => Access.WRITE_ALL_BUSINESS
            case Access.CREATE => Access.CREATE_BUSINESS
            case _ => access
          }
          case TManagementPolicy(_) => access match { // an agbot accessing a management policy
            case Access.READ => Access.READ_ALL_MANAGEMENT_POLICY
            case Access.WRITE => Access.WRITE_ALL_MANAGEMENT_POLICY
            case Access.CREATE => Access.CREATE_MANAGEMENT_POLICY
            case _ => access
          }
          case TOrg(_) => access match { // an agbot accessing his org resource
            case Access.READ => Access.READ_MY_ORG
            case Access.WRITE => Access.WRITE_MY_ORG
            case Access.CREATE => Access.CREATE_ORGS
            case _ => access
          }
          case TAction(_) => access // an agbot running an action
        }
      } else if (!target.isThere && access == Access.READ) {  // not my org, not public, not there, and we are trying to read it
        Access.NOT_FOUND
      } else {  // not my org and not public, not a msg send to node, but is there or we might create it
        access match {
          case Access.READ => Access.READ_OTHER_ORGS
          case Access.WRITE => Access.WRITE_OTHER_ORGS
          case Access.CREATE => Access.CREATE_IN_OTHER_ORGS
          case _ => access
        }
      }
    if (requiredAccess == Access.NOT_FOUND) Failure(new ResourceNotFoundException(ExchMsg.translate("resource.not.found", target.id)))
    else if (Role.hasAuthorization(role, requiredAccess)) Success(this)
    else Failure(new AccessDeniedException(accessDeniedMsg(requiredAccess, target)))
  }

  override def isMultiTenantAgbot: Boolean = getOrg == "IBM"    //someday: implement instance-level ACLs instead of hardcoding this
}
