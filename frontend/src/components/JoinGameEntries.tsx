import { useState } from "react";
import { validate } from "uuid";

export function JoinGameEntries({ onJoinClicked }: JoinGameEntriesProps) {
  const [gameId, setGameId] = useState("");
  function handleOnClick() {
    if (validate(gameId)) {
      onJoinClicked(gameId);
    } else {
      alert("invalid gameId");
    }
  }
  return (
    <div className="flex col pt-xl5">
      <label htmlFor="gameId">Enter game id</label>
      <input
        placeholder="UUID"
        id="gameId"
        onChange={(e) => setGameId(e.target.value)}
      />
      <button onClick={handleOnClick}>Join</button>
    </div>
  );
}

interface JoinGameEntriesProps {
  onJoinClicked: (gameId: string) => void;
}
