"use client";

import { useCallback, useEffect, useMemo, useRef, useState } from "react";
import {
  Alert,
  Box,
  Button,
  ButtonBase,
  CircularProgress,
  LinearProgress,
  Paper,
  Stack,
  Typography,
} from "@mui/material";
import CheckCircleIcon from "@mui/icons-material/CheckCircle";
import type {
  Answer,
  MiroAnswer,
  Progress,
  PublicQuestion,
  TrueFalseAnswer,
} from "@/lib/assessment/survey";

interface Props {
  token: string;
  firstName: string;
  questions: PublicQuestion[];
  initialProgress: Progress;
  practitioner: { name: string; email: string; phone: string | null } | null;
}

type Stage = "intro" | "questions" | "submitting" | "done" | "retake";
type SaveState = "idle" | "saving" | "saved" | "error";

const MIRO_GRADIENT =
  "linear-gradient(90deg,#d53f35 0 25%,#fbc726 25% 50%,#43add5 50% 75%,#42b449 75% 100%)";

/** A word question only counts once both "most" and "least" are picked. */
function isAnswered(q: PublicQuestion, progress: Progress) {
  if (q.kind === "info") return true;
  const answer = progress[q.id];
  if (!answer) return false;
  if (q.kind === "miro") {
    const { most, least } = answer as MiroAnswer;
    return Boolean(most && least);
  }
  return Boolean((answer as TrueFalseAnswer).choice);
}

export default function AssessmentRunner({
  token,
  firstName,
  questions,
  initialProgress,
  practitioner,
}: Props) {
  const [progress, setProgress] = useState<Progress>(initialProgress);
  const [stage, setStage] = useState<Stage>("intro");
  const [index, setIndex] = useState(() => {
    const firstOpen = questions.findIndex((q) => !isAnswered(q, initialProgress));
    return firstOpen === -1 ? questions.length - 1 : firstOpen;
  });
  const [saveState, setSaveState] = useState<SaveState>("idle");
  const [error, setError] = useState("");
  const headingRef = useRef<HTMLHeadingElement>(null);

  const answerable = useMemo(() => questions.filter((q) => q.kind !== "info"), [questions]);
  const miroCount = answerable.filter((q) => q.kind === "miro").length;
  const tfCount = answerable.length - miroCount;
  const answeredCount = answerable.filter((q) => isAnswered(q, progress)).length;
  const started = answeredCount > 0;
  const question = questions[index];
  const answerNumber = answerable.findIndex((q) => q.id === question?.id) + 1;
  const isLast = index === questions.length - 1;
  const allAnswered = answeredCount === answerable.length;

  useEffect(() => {
    if (stage === "questions") headingRef.current?.focus();
  }, [index, stage]);

  const save = useCallback(
    async (questionId: string, answer: Answer) => {
      setSaveState("saving");
      setError("");
      try {
        const response = await fetch(`/api/assess/${token}/answers`, {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({ questionId, answer }),
        });
        if (!response.ok) {
          const body = await response.json().catch(() => ({}));
          throw new Error(body.error || "We couldn't save that answer.");
        }
        setSaveState("saved");
        return true;
      } catch (e) {
        setSaveState("error");
        setError(`${(e as Error).message} Please check your connection and try again.`);
        return false;
      }
    },
    [token],
  );

  function setAnswer(q: PublicQuestion, answer: Answer) {
    setProgress((p) => ({ ...p, [q.id]: answer }));
    const complete =
      q.kind === "truefalse" || ((answer as MiroAnswer).most && (answer as MiroAnswer).least);
    if (complete) void save(q.id, answer);
  }

  function goNext() {
    if (!isLast) setIndex((i) => i + 1);
  }

  async function chooseTrueFalse(q: PublicQuestion, optionId: string) {
    setAnswer(q, { choice: optionId });
    // Small pause so the choice is visible before moving on.
    setTimeout(
      () => setIndex((i) => (i === questions.indexOf(q) && i < questions.length - 1 ? i + 1 : i)),
      280,
    );
  }

  async function postComplete() {
    const response = await fetch(`/api/assess/${token}/complete`, { method: "POST" });
    const body = await response.json().catch(() => ({}));
    return { response, body };
  }

  async function submit() {
    setStage("submitting");
    setError("");
    try {
      let { response, body } = await postComplete();

      // The server is missing some answers (e.g. a save failed): resend the ones
      // this page already has, one at a time, then try again.
      if (response.status === 409 && Array.isArray(body.missingQuestionIds)) {
        const missing: string[] = body.missingQuestionIds;
        const complete = (id: string) => {
          const q = questions.find((x) => x.id === id);
          return Boolean(q && isAnswered(q, progress));
        };
        const resendable = missing.filter(complete);
        for (const id of resendable) {
          if (!(await save(id, progress[id])))
            throw new Error("We couldn't save all your answers.");
        }
        if (resendable.length === missing.length) {
          ({ response, body } = await postComplete());
        } else {
          const first = questions.findIndex((q) => q.id === missing.find((id) => !complete(id)));
          if (first >= 0) setIndex(first);
          throw new Error("This question still needs an answer.");
        }
      }

      if (!response.ok) throw new Error(body.error || "We couldn't submit your assessment.");
      if (body.status === "uninterpretable") {
        setProgress({});
        setIndex(0);
        setStage("retake");
        return;
      }
      setStage("done");
    } catch (e) {
      setError((e as Error).message);
      setStage("questions");
    }
  }

  // ---------------------------------------------------------------------------

  if (stage === "intro" || stage === "retake") {
    return (
      <Paper sx={{ p: { xs: 3, sm: 5 }, overflow: "hidden", position: "relative" }}>
        <Box
          sx={{ position: "absolute", inset: "0 0 auto 0", height: 6, background: MIRO_GRADIENT }}
        />
        <Stack spacing={2.25}>
          {stage === "retake" ? (
            <Alert severity="warning">
              Unfortunately your answers gave a result that can&apos;t be interpreted — all your
              scores came out in the same range. This usually happens when the questions are given
              too much thought. Please try again, go with your instinct, and think about how you are
              at work.
            </Alert>
          ) : null}
          <Typography variant="h4" component="h1" sx={{ color: "text.primary", letterSpacing: 0 }}>
            {stage === "retake" ? "Let's try that again" : `Hello ${firstName}`}
          </Typography>
          <Typography>
            You&apos;re about to complete the MiRo Behavioural Mode Assessment. There are{" "}
            <strong>{answerable.length} questions</strong>
            {tfCount
              ? ` — ${miroCount} word choices, then ${tfCount} true or false statements`
              : ""}
            .
          </Typography>
          <Typography>
            For each set of four words, pick the one that <strong>most</strong> describes you and
            the one that <strong>least</strong> describes you. Sometimes they may all seem to fit,
            or none do — go with your first instinct, or think about what someone who knows you well
            might say.
          </Typography>
          <Typography>
            <strong>There are no right or wrong answers</strong>, and no-one else will see your
            responses. Try to finish within about 15 minutes. Your answers are saved as you go, so
            you can close this page and come back to your link later.
          </Typography>
          {practitioner ? (
            <Typography color="text.secondary">
              Your practitioner {practitioner.name} will receive your report and go through it with
              you.
            </Typography>
          ) : null}
          <Box>
            <Button size="large" variant="contained" onClick={() => setStage("questions")}>
              {started && stage === "intro"
                ? `Continue (${answeredCount} of ${answerable.length} done)`
                : "Start my assessment"}
            </Button>
          </Box>
        </Stack>
      </Paper>
    );
  }

  if (stage === "done") {
    return (
      <Paper sx={{ p: { xs: 3, sm: 5 } }}>
        <Stack spacing={2} alignItems="flex-start">
          <CheckCircleIcon sx={{ fontSize: 48, color: "#42b449" }} />
          <Typography variant="h4" component="h1" sx={{ color: "text.primary", letterSpacing: 0 }}>
            Thank you, {firstName}
          </Typography>
          <Typography>
            You have completed the MiRo Behavioural Mode Assessment. Your results will be passed on
            to you by your practitioner{practitioner ? `, ${practitioner.name}` : ""}.
          </Typography>
          {practitioner?.email ? (
            <Typography color="text.secondary">
              Any questions? Contact {practitioner.email}
              {practitioner.phone ? ` or ${practitioner.phone}` : ""}.
            </Typography>
          ) : null}
        </Stack>
      </Paper>
    );
  }

  const percent = Math.round((answeredCount / Math.max(answerable.length, 1)) * 100);

  return (
    <Stack spacing={2}>
      <Box>
        <Stack direction="row" justifyContent="space-between" sx={{ mb: 0.75 }}>
          <Typography variant="body2" color="text.secondary">
            {question.kind === "info"
              ? "A short pause"
              : `Question ${answerNumber} of ${answerable.length}`}
          </Typography>
          <Typography variant="body2" color="text.secondary" aria-live="polite">
            {saveState === "saving" ? "Saving…" : saveState === "saved" ? "Saved" : ""}
          </Typography>
        </Stack>
        <LinearProgress
          variant="determinate"
          value={percent}
          aria-label={`${percent}% complete`}
          sx={{
            height: 8,
            borderRadius: 4,
            bgcolor: "#e4e6ea",
            "& .MuiLinearProgress-bar": { background: MIRO_GRADIENT },
          }}
        />
      </Box>

      <Paper sx={{ p: { xs: 2.5, sm: 4 } }}>
        {question.kind === "miro" ? (
          <MiroQuestion
            key={question.id}
            question={question}
            answer={progress[question.id] as MiroAnswer | undefined}
            headingRef={headingRef}
            onChange={(a) => setAnswer(question, a as Answer)}
          />
        ) : question.kind === "truefalse" ? (
          <TrueFalseQuestion
            key={question.id}
            question={question}
            answer={progress[question.id] as TrueFalseAnswer | undefined}
            headingRef={headingRef}
            onChoose={(id) => chooseTrueFalse(question, id)}
          />
        ) : (
          <Stack spacing={2}>
            <Typography
              ref={headingRef}
              tabIndex={-1}
              variant="h5"
              component="h2"
              sx={{ color: "text.primary", outline: "none" }}
            >
              Nearly there
            </Typography>
            <Typography sx={{ fontSize: 17 }}>{question.text}</Typography>
          </Stack>
        )}
      </Paper>

      {error ? (
        <Alert
          severity="error"
          action={
            saveState === "error" && isAnswered(question, progress) ? (
              <Button
                color="inherit"
                size="small"
                onClick={() => save(question.id, progress[question.id])}
              >
                Retry
              </Button>
            ) : undefined
          }
        >
          {error}
        </Alert>
      ) : null}

      <Stack direction="row" justifyContent="space-between" alignItems="center">
        <Button
          onClick={() => setIndex((i) => Math.max(0, i - 1))}
          disabled={index === 0 || stage === "submitting"}
        >
          Back
        </Button>
        {isLast ? (
          <Button
            variant="contained"
            size="large"
            onClick={submit}
            disabled={!allAnswered || stage === "submitting" || saveState === "saving"}
            startIcon={
              stage === "submitting" ? <CircularProgress size={16} color="inherit" /> : undefined
            }
          >
            {stage === "submitting" ? "Submitting…" : "Finish and submit"}
          </Button>
        ) : (
          <Button
            variant="contained"
            size="large"
            onClick={goNext}
            disabled={!isAnswered(question, progress) || saveState === "error"}
          >
            {question.kind === "info" ? "Continue" : "Next"}
          </Button>
        )}
      </Stack>
      {isLast && !allAnswered ? (
        <Stack direction="row" spacing={1} justifyContent="flex-end" alignItems="center">
          <Typography variant="body2" color="text.secondary">
            {answerable.length - answeredCount === 1
              ? "1 question still needs an answer."
              : `${answerable.length - answeredCount} questions still need an answer.`}
          </Typography>
          <Button
            size="small"
            onClick={() => setIndex(questions.findIndex((q) => !isAnswered(q, progress)))}
          >
            Go to it
          </Button>
        </Stack>
      ) : null}
    </Stack>
  );
}

// ---------------------------------------------------------------------------

function ChoiceButton({
  label,
  selected,
  disabled,
  colour,
  onClick,
  ariaLabel,
}: {
  label: string;
  selected: boolean;
  disabled?: boolean;
  colour: string;
  onClick: () => void;
  ariaLabel: string;
}) {
  return (
    <ButtonBase
      onClick={onClick}
      disabled={disabled}
      aria-pressed={selected}
      aria-label={ariaLabel}
      focusRipple
      sx={{
        justifyContent: "center",
        minHeight: 56,
        px: 2,
        borderRadius: 2,
        border: "2px solid",
        borderColor: selected ? colour : "#dfe2e6",
        bgcolor: selected ? colour : "#fff",
        color: selected ? "#fff" : "text.primary",
        fontSize: 17,
        fontWeight: selected ? 600 : 400,
        transition: "all .15s ease",
        opacity: disabled ? 0.35 : 1,
        "&:hover": { borderColor: disabled ? "#dfe2e6" : colour },
        "&.Mui-focusVisible": { outline: `3px solid ${colour}55`, outlineOffset: 2 },
      }}
    >
      {label}
    </ButtonBase>
  );
}

function MiroQuestion({
  question,
  answer,
  headingRef,
  onChange,
}: {
  question: PublicQuestion;
  answer?: MiroAnswer;
  headingRef: React.RefObject<HTMLHeadingElement | null>;
  onChange: (a: Partial<MiroAnswer>) => void;
}) {
  const [most, setMost] = useState(answer?.most ?? "");
  const [least, setLeast] = useState(answer?.least ?? "");

  function pick(kind: "most" | "least", id: string) {
    const next = { most, least, [kind]: id };
    // Picking the same word for both moves it; the other choice is cleared.
    if (kind === "most" && id === least) next.least = "";
    if (kind === "least" && id === most) next.most = "";
    setMost(next.most);
    setLeast(next.least);
    onChange(next);
  }

  const grid = {
    display: "grid",
    gridTemplateColumns: { xs: "1fr 1fr", sm: "repeat(4, 1fr)" },
    gap: 1.25,
  };

  return (
    <Stack spacing={3}>
      <Box>
        <Typography
          ref={headingRef}
          tabIndex={-1}
          variant="h6"
          component="h2"
          sx={{ color: "text.primary", outline: "none", mb: 1.5 }}
        >
          Which word <strong>most</strong> describes you?
        </Typography>
        <Box sx={grid} role="group" aria-label="Most like me">
          {question.options.map((o) => (
            <ChoiceButton
              key={o.id}
              label={o.label}
              ariaLabel={`Most: ${o.label}`}
              selected={most === o.id}
              colour="#288eb5"
              onClick={() => pick("most", o.id)}
            />
          ))}
        </Box>
      </Box>
      <Box>
        <Typography variant="h6" component="h3" sx={{ color: "text.primary", mb: 1.5 }}>
          Which word <strong>least</strong> describes you?
        </Typography>
        <Box sx={grid} role="group" aria-label="Least like me">
          {question.options.map((o) => (
            <ChoiceButton
              key={o.id}
              label={o.label}
              ariaLabel={`Least: ${o.label}`}
              selected={least === o.id}
              disabled={most === o.id}
              colour="#6b6b6b"
              onClick={() => pick("least", o.id)}
            />
          ))}
        </Box>
      </Box>
    </Stack>
  );
}

function TrueFalseQuestion({
  question,
  answer,
  headingRef,
  onChoose,
}: {
  question: PublicQuestion;
  answer?: TrueFalseAnswer;
  headingRef: React.RefObject<HTMLHeadingElement | null>;
  onChoose: (optionId: string) => void;
}) {
  return (
    <Stack spacing={3}>
      <Typography
        ref={headingRef}
        tabIndex={-1}
        variant="h6"
        component="h2"
        sx={{ color: "text.primary", outline: "none", fontWeight: 400, fontSize: 20 }}
      >
        {question.text}
      </Typography>
      <Box
        sx={{ display: "grid", gridTemplateColumns: "1fr 1fr", gap: 1.5, maxWidth: 420 }}
        role="group"
        aria-label="True or false"
      >
        {question.options.map((o) => (
          <ChoiceButton
            key={o.id}
            label={o.label}
            ariaLabel={o.label}
            selected={answer?.choice === o.id}
            colour={o.label === "True" ? "#42b449" : "#d53f35"}
            onClick={() => onChoose(o.id)}
          />
        ))}
      </Box>
    </Stack>
  );
}
