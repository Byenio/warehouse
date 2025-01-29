"use server";

import * as z from "zod";
import { env } from "process";

const formSchema = z.object({
  name: z.string(),
  description: z.string(),
  imageUrl: z.string(),
  vatPercentage: z.string(),
  grossPriceInCents: z.string(),
  stock: z.string(),
  subcategory: z.string(),
  manufacturer: z.string(),
  warehouse: z.string(),
});

export async function submitForm(values: z.infer<typeof formSchema>) {
  console.log(values);
  try {
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

    return { success: true, message: "Dane wysłane pomyślnie!" };
  } catch (error) {
    return { success: false, message: (error as Error).message };
  }
}
