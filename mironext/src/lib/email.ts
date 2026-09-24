// Outgoing email. Uses Resend when RESEND_API_KEY is set; otherwise (local dev)
// the message is logged to the server console instead of being sent.

import { Resend } from "resend";

export interface EmailMessage {
  to: string;
  subject: string;
  html: string;
  text: string;
  bcc?: string[];
  replyTo?: string;
}

export interface EmailResult {
  delivered: boolean;
  /** "resend" when sent, "console" when only logged (no API key). */
  transport: "resend" | "console";
  id?: string;
}

let client: Resend | null = null;

export function isEmailConfigured(): boolean {
  return Boolean(process.env.RESEND_API_KEY);
}

export async function sendEmail(message: EmailMessage): Promise<EmailResult> {
  if (!isEmailConfigured()) {
    console.info(
      `[email:console] to=${message.to} subject=${JSON.stringify(message.subject)}\n${message.text}`,
    );
    return { delivered: false, transport: "console" };
  }

  client ??= new Resend(process.env.RESEND_API_KEY);
  const from = process.env.EMAIL_FROM || "MiRo <no-reply@miro-assessment.com>";
  const { data, error } = await client.emails.send({
    from,
    to: [message.to],
    bcc: message.bcc?.length ? message.bcc : undefined,
    replyTo: message.replyTo,
    subject: message.subject,
    html: message.html,
    text: message.text,
  });
  if (error) {
    throw new Error(`Email send failed: ${error.message}`);
  }
  return { delivered: true, transport: "resend", id: data?.id };
}

export function escapeHtml(s: string): string {
  return s
    .replace(/&/g, "&amp;")
    .replace(/</g, "&lt;")
    .replace(/>/g, "&gt;")
    .replace(/"/g, "&quot;");
}
