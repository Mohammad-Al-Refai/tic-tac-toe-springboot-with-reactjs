import { useEffect } from "react";

export function CreateGameEntries({
  onCreateClicked,
  joinGame,
  gameId,
}: CreateGameEntriesProps) {
  useEffect(() => {
    console.log("HI");
    if (gameId == "") {
      return;
    }
    joinGame(gameId);
  }, [gameId]);
  return (
    <div className="flex col pt-xl5">
      <button onClick={onCreateClicked}>Create Game</button>
    </div>
  );
}

interface CreateGameEntriesProps {
  onCreateClicked: () => void;
  gameId: string;
  joinGame: (gameId: string) => void;
}
