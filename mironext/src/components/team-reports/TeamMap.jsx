"use client";

import { useMemo, useState } from "react";
import { Alert, Box, FormControlLabel, Stack, Switch, Typography } from "@mui/material";
import { plotTeamMap, TEAM_MAP_BACKGROUND, TEAM_MAP_HEIGHT, TEAM_MAP_WIDTH } from "@/lib/team-map";

const BORDER_WIDTH = 2;

export default function TeamMap({ members }) {
  const [engaged, setEngaged] = useState(true);
  const { dots, radius, fontSize, omitted } = useMemo(
    () => plotTeamMap(members, { engaged }),
    [members, engaged],
  );
  const namesByInitials = useMemo(
    () => new Map(members.map((member) => [member.initials, member.fullName])),
    [members],
  );

  if (members.length === 0) {
    return (
      <Typography color="text.secondary">
        No candidates in these projects have a purchased report yet, so there is nothing to plot.
      </Typography>
    );
  }

  return (
    <Stack spacing={1}>
      <FormControlLabel
        control={
          <Switch checked={engaged} onChange={(event) => setEngaged(event.target.checked)} />
        }
        label="Show secondary engaged modes"
      />
      <Box sx={{ width: "100%", maxWidth: 900 }}>
        <svg
          viewBox={`0 0 ${TEAM_MAP_WIDTH} ${TEAM_MAP_HEIGHT}`}
          width="100%"
          role="img"
          aria-label="Team map"
          style={{ display: "block" }}
        >
          <image
            href={TEAM_MAP_BACKGROUND}
            x={0}
            y={0}
            width={TEAM_MAP_WIDTH}
            height={TEAM_MAP_HEIGHT}
          />
          {dots.map((dot) => (
            <g key={`${dot.initials}-${dot.leading ? "leading" : "secondary"}`}>
              <title>
                {`${namesByInitials.get(dot.initials) || dot.initials} (${dot.leading ? "leading" : "secondary"})`}
              </title>
              <circle cx={dot.x} cy={dot.y} r={radius} fill="#fff" />
              <circle cx={dot.x} cy={dot.y} r={radius - BORDER_WIDTH} fill="#000" />
              <text
                x={dot.x}
                y={dot.y}
                fill="#fff"
                fontSize={fontSize}
                fontWeight="bold"
                fontFamily="Calibri, Arial, sans-serif"
                textAnchor="middle"
                dominantBaseline="central"
              >
                {dot.initials}
              </text>
            </g>
          ))}
        </svg>
      </Box>
      {omitted > 0 ? (
        <Alert severity="warning">
          {omitted} {omitted === 1 ? "position was" : "positions were"} left off the map because a
          quadrant is full.
        </Alert>
      ) : null}
    </Stack>
  );
}
