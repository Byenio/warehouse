"use server";

import * as z from "zod";
import { env } from "process";
import { redirect } from "next/navigation";

const formSchema = z.object({
  name: z.string(),
  description: z.string(),
  imageUrl: z.string(),
  vatPercentage: z.string(),
  grossPriceInCents: z.string(),
  stock: z.string(),
  subcategoryId: z.string(),
  manufacturerId: z.string(),
  warehouseId: z.string(),
});

export async function submitForm(values: z.infer<typeof formSchema>) {
  const response = await fetch(
    `http://${env.SERVER_ADDRESS}:${env.SERVER_PORT}/api/product`,
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
  redirect("/");
}
