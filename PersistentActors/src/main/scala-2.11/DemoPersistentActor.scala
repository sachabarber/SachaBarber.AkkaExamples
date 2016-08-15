import akka.actor._
import akka.persistence._



class DemoPersistentActor extends PersistentActor {

  //note : This is  mutable
  var state = ExampleState()

  def updateState(event: Evt): Unit =
    state = state.updated(event)

  def numEvents =
    state.size

  val receiveRecover: Receive = {
    case evt: Evt => updateState(evt)
    case SnapshotOffer(_, snapshot: ExampleState) => {
        println(s"offered state = $snapshot")
        state = snapshot
    }
  }

  val receiveCommand: Receive = {
    case Cmd(data) =>
      persist(Evt(s"${data}-${numEvents}"))(updateState)
      persist(Evt(s"${data}-${numEvents + 1}")) { event =>
        updateState(event)
        context.system.eventStream.publish(event)
      }
    case "snap"  => saveSnapshot(state)
    case SaveSnapshotSuccess(metadata) =>
      println(s"SaveSnapshotSuccess(metadata) :  metadata=$metadata")
    case SaveSnapshotFailure(metadata, reason) =>
      println("""SaveSnapshotFailure(metadata, reason) :
        metadata=$metadata, reason=$reason""")
    case "print" => println(state)
    case "boom"  => throw new Exception("boom")
  }

  override def persistenceId = "demo-persistent-actor-1"
}
