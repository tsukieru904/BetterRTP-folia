# BetterRTP custom modernization patch

This fork includes fixes targeting GitHub reports #257, #256, #254, #253,
#259, #251, #244, #242 and #238.

- Folia/Paper modern teleport uses `Player#teleportAsync` when available and
  re-enters the player's Folia entity scheduler before touching player/world state.
- First-join RTP is deferred to the player's entity scheduler and explicitly
  bypasses the configurable RTP delay.
- Queue safe-location checks run on the region that owns the target location.
- Startup queue generation skips empty worlds to avoid unnecessary VoidGen/
  Multiverse region generation; normal RTP requests can replenish the queue.
- PlaceholderAPI internal expansion is persistent across plugin reload.
- SQLite dynamic world table names and queue/world values are quoted/prepared,
  including world names containing spaces.
- MinePlots lookup uses Bukkit's service loader API and fails safely when the
  service is unavailable.
- Sound playback prefers the resource-key String overload on modern versions
  and never calls `playSound` with a null Sound enum.
- Documentation lists server families 1.8 through 1.21, 26.1 and 26.2, with
  Folia support declared for 1.21, 26.1 and 26.2.
