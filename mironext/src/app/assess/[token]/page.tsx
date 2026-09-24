import type { Metadata } from "next";
import { Box, Container, Paper, Stack, Typography } from "@mui/material";
import Image from "next/image";
import AssessmentRunner from "@/components/assessment/AssessmentRunner";
import { getPractitionerForProject, loadSurvey, resolveInvite } from "@/lib/assessment/service";
import { toPublicQuestions, type Progress } from "@/lib/assessment/survey";

export const dynamic = "force-dynamic";

export const metadata: Metadata = {
  title: "MiRo Assessment",
  robots: { index: false, follow: false },
  // Keep the token out of Referer headers sent to other sites.
  referrer: "no-referrer",
};

type Params = { params: Promise<{ token: string }> };

function Shell({ children }: { children: React.ReactNode }) {
  return (
    <Box sx={{ minHeight: "100vh", bgcolor: "#f4f5f7" }}>
      <Box sx={{ bgcolor: "#fff", borderBottom: "1px solid #e9e9e9" }}>
        <Container maxWidth="md" sx={{ py: 1.5 }}>
          <Image src="/miro-logo-small.png" alt="MiRo" width={95} height={41} priority />
        </Container>
      </Box>
      <Container maxWidth="md" sx={{ py: { xs: 2, sm: 5 } }}>
        {children}
      </Container>
    </Box>
  );
}

function Message({ title, children }: { title: string; children: React.ReactNode }) {
  return (
    <Shell>
      <Paper sx={{ p: { xs: 3, sm: 5 } }}>
        <Stack spacing={2}>
          <Typography variant="h5" component="h1" sx={{ color: "text.primary" }}>
            {title}
          </Typography>
          {children}
        </Stack>
      </Paper>
    </Shell>
  );
}

export default async function AssessPage({ params }: Params) {
  const { token } = await params;
  const resolved = await resolveInvite(token);

  if (resolved.state === "invalid") {
    return (
      <Message title="This link isn't valid">
        <Typography>
          The assessment link may have been mistyped or replaced by a newer invite. Please use the
          most recent email from your MiRo practitioner, or ask them to send a new invite.
        </Typography>
      </Message>
    );
  }

  const practitioner = await getPractitionerForProject(resolved.invite.candidate.projectId);
  const practitionerName = practitioner ? `${practitioner.firstName} ${practitioner.lastName}` : "";

  if (resolved.state === "expired") {
    return (
      <Message title="This link has expired">
        <Typography>
          Please ask {practitionerName || "your MiRo practitioner"} to send you a new invite. Any
          answers you already gave will be kept.
        </Typography>
      </Message>
    );
  }

  if (resolved.state === "completed") {
    return (
      <Message title="Thank you — your assessment is complete">
        <Typography>
          Your results will be passed on to you by your MiRo practitioner
          {practitionerName ? `, ${practitionerName}` : ""}.
        </Typography>
        {practitioner?.email ? (
          <Typography color="text.secondary">
            Questions? Contact {practitioner.email}
            {practitioner.phoneNumber ? ` or ${practitioner.phoneNumber}` : ""}.
          </Typography>
        ) : null}
      </Message>
    );
  }

  const survey = await loadSurvey(resolved.invite.surveyId);
  return (
    <Shell>
      <AssessmentRunner
        token={token}
        firstName={resolved.invite.candidate.firstName}
        questions={toPublicQuestions(survey)}
        initialProgress={(resolved.invite.progress as Progress) ?? {}}
        practitioner={
          practitioner
            ? {
                name: practitionerName,
                email: practitioner.email,
                phone: practitioner.phoneNumber ?? null,
              }
            : null
        }
      />
    </Shell>
  );
}
