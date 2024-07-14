export function AvailableGamesList({
  games,
  onRefresh,
}: AvailableGamesListProps) {
  return (
    <div className="flex col align-items-center">
      <button onClick={onRefresh}>Refresh</button>
      <div className="flex col align-items-center">
        {games.map((id) => {
          return <GameItem key={id} gameId={id} />;
        })}
      </div>
    </div>
  );
}
function GameItem({ gameId }: GameItemProps) {
  return <div>{gameId}</div>;
}
interface AvailableGamesListProps {
  onRefresh: () => void;
  games: string[];
}
interface GameItemProps {
  gameId: string;
}
