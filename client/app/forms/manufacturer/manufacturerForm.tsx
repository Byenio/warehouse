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
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { submitForm } from "./submitForm";
import { Alert, AlertDescription, AlertTitle } from "@/components/ui/alert";
import { useState } from "react";

const formSchema = z.object({
  name: z.string(),
  description: z.string(),
  code: z.string(),
  logoUrl: z.string(),
  website: z.string(),
  countryId: z.string(),
});

export default function ManufacturerForm({
  formData,
}: {
  formData: CountryInfo[];
}) {
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

          <FormField
            control={form.control}
            name="code"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Kod</FormLabel>
                <FormControl>
                  <Input placeholder="kod" type="number" {...field} />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />

          <FormField
            control={form.control}
            name="logoUrl"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Logo URL</FormLabel>
                <FormControl>
                  <Input placeholder="url" type="text" {...field} />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />

          <FormField
            control={form.control}
            name="website"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Strona internetowa</FormLabel>
                <FormControl>
                  <Input
                    placeholder="Strona internetowa"
                    type="text"
                    {...field}
                  />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />

          <FormField
            control={form.control}
            name="countryId"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Kraj</FormLabel>
                <Select
                  onValueChange={field.onChange}
                  defaultValue={field.value}
                >
                  <FormControl>
                    <SelectTrigger>
                      <SelectValue placeholder="Wybierz kraj" />
                    </SelectTrigger>
                  </FormControl>

                  <SelectContent>
                    {formData.map((country) => (
                      <SelectItem key={country.id} value={country.id}>
                        {country.name}
                      </SelectItem>
                    ))}
                  </SelectContent>
                </Select>
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
            Producent {form.getValues().name} został dodany pomyślnie.
          </AlertDescription>
        </Alert>
      )}
      {status === "error" && (
        <Alert>
          <AlertTitle>Komunikat</AlertTitle>
          <AlertDescription>
            Producent {form.getValues().name} nie został dodany pomyślnie.
          </AlertDescription>
        </Alert>
      )}
    </>
  );
}
