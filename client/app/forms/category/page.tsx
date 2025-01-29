"use client";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import * as z from "zod";
import { Button } from "@/components/ui/button";
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/components/ui/form";
import { Input } from "@/components/ui/input";
import { submitForm } from "./submitForm";
import { Alert, AlertDescription, AlertTitle } from "@/components/ui/alert";
import { useState } from "react";
const formSchema = z.object({
  name: z.string(),
  description: z.string(),
});

export default function CategoryForm() {
  const form = useForm<z.infer<typeof formSchema>>({
    resolver: zodResolver(formSchema),
  });
  const [status, setStatus] = useState<"success" | "error" | null>(null);
  function onSubmit(values: z.infer<typeof formSchema>) {
    try {
      submitForm(values);
      setStatus("success");
    } catch (error) {
      console.log(error);
      setStatus("error");
    }
  }

  return (
    <>
      <Form {...form}>
        <form
          onSubmit={form.handleSubmit(() => onSubmit(form.getValues()))}
          className="space-y-8 max-w-3xl mx-auto py-10"
        >
          <FormField
            control={form.control}
            name="name"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Nazwa</FormLabel>
                <FormControl>
                  <Input placeholder="nazwa" type="text" {...field} />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />

          <FormField
            control={form.control}
            name="description"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Opis</FormLabel>
                <FormControl>
                  <Input placeholder="opis" type="text" {...field} />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />
          <Button type="submit">Dodaj</Button>
        </form>
      </Form>
      {status === "success" && (
        <Alert>
          <AlertTitle>Komunikat</AlertTitle>
          <AlertDescription>
            Kategoria {form.getValues().name} została dodana pomyślnie.
          </AlertDescription>
        </Alert>
      )}
      {status === "error" && (
        <Alert>
          <AlertTitle>Komunikat</AlertTitle>
          <AlertDescription>
            Kategoria {form.getValues().name} nie została dodana pomyślnie.
          </AlertDescription>
        </Alert>
      )}
    </>
  );
}
