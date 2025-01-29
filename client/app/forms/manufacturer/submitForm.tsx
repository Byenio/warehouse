"use server";

import * as z from "zod";
import { env } from "process";

const formSchema = z.object({
  name: z.string(),
  description: z.string(),
  code: z.string(),
  logoUrl: z.string(),
  website: z.string(),
  countryId: z.string(),
});

export async function submitForm(values: z.infer<typeof formSchema>) {
  const response = await fetch(
    `http://${env.SERVER_ADDRESS}:${env.SERVER_PORT}/api/manufacturer`,
    {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(values),
    }
  );

  if (!response.ok) {
    throw new Error("Błąd wysyłania danych");
  }
}
