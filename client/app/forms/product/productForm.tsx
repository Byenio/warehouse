"use client";

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
import { zodResolver } from "@hookform/resolvers/zod";
import { useForm } from "react-hook-form";
import * as z from "zod";
import { submitForm } from "./submitForm";
import { Alert, AlertDescription, AlertTitle } from "@/components/ui/alert";
import { useState } from "react";

const formSchema = z.object({
  name: z.string(),
  description: z.string(),
  imageUrl: z.string(),
  barcode: z.string().optional(),
  vatPercentage: z.string(),
  grossPriceInCents: z.string(),
  stock: z.string(),
  subcategoryId: z.string(),
  manufacturerId: z.string(),
  warehouseId: z.string(),
});

export default function ProductForm({
  manufacturerData,
  subcategoryData,
  warehouseData,
}: {
  manufacturerData: ManufacturerInfo[];
  subcategoryData: SubcategoryInfo[];
  warehouseData: Warehouse[];
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
            name="imageUrl"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Adres URL zdjęcia</FormLabel>
                <FormControl>
                  <Input placeholder="url" type="text" {...field} />
                </FormControl>

                <FormMessage />
              </FormItem>
            )}
          />
          <FormField
            control={form.control}
            name="barcode"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Kod kreskowy</FormLabel>
                <FormControl>
                  <Input placeholder="barcode" type="text" {...field} />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />

          <FormField
            control={form.control}
            name="vatPercentage"
            render={({ field }) => (
              <FormItem>
                <FormLabel>VAT</FormLabel>
                <FormControl>
                  <Input placeholder="vat" type="number" {...field} />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />

          <FormField
            control={form.control}
            name="grossPriceInCents"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Cena Brutto</FormLabel>
                <FormControl>
                  <Input placeholder="brutto" type="number" {...field} />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />

          <FormField
            control={form.control}
            name="stock"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Ilość</FormLabel>
                <FormControl>
                  <Input placeholder="ilość" type="number" {...field} />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />

          <FormField
            control={form.control}
            name="subcategoryId"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Podkategoria</FormLabel>
                <Select
                  onValueChange={field.onChange}
                  defaultValue={field.value}
                >
                  <FormControl>
                    <SelectTrigger>
                      <SelectValue placeholder="Wybierz podkategorię" />
                    </SelectTrigger>
                  </FormControl>
                  <SelectContent>
                    {subcategoryData.map((subcategory) => (
                      <SelectItem key={subcategory.id} value={subcategory.id}>
                        {subcategory.name}
                      </SelectItem>
                    ))}
                  </SelectContent>
                </Select>
                <FormMessage />
              </FormItem>
            )}
          />

          <FormField
            control={form.control}
            name="manufacturerId"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Producent</FormLabel>
                <Select
                  onValueChange={field.onChange}
                  defaultValue={field.value}
                >
                  <FormControl>
                    <SelectTrigger>
                      <SelectValue placeholder="Wybierz producenta" />
                    </SelectTrigger>
                  </FormControl>
                  <SelectContent>
                    {manufacturerData.map((manufacturer) => (
                      <SelectItem key={manufacturer.id} value={manufacturer.id}>
                        {manufacturer.name}
                      </SelectItem>
                    ))}
                  </SelectContent>
                </Select>
                <FormMessage />
              </FormItem>
            )}
          />

          <FormField
            control={form.control}
            name="warehouseId"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Magazyn</FormLabel>
                <Select
                  onValueChange={field.onChange}
                  defaultValue={field.value}
                >
                  <FormControl>
                    <SelectTrigger>
                      <SelectValue placeholder="Wybierz magazyn" />
                    </SelectTrigger>
                  </FormControl>
                  <SelectContent>
                    {warehouseData.map((warehouse) => (
                      <SelectItem key={warehouse.id} value={warehouse.id}>
                        {warehouse.name}
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
            Produkt {form.getValues().name} został dodany pomyślnie.
          </AlertDescription>
        </Alert>
      )}
      {status === "error" && (
        <Alert>
          <AlertTitle>Komunikat</AlertTitle>
          <AlertDescription>
            Produkt {form.getValues().name} nie został dodany pomyślnie.
          </AlertDescription>
        </Alert>
      )}
    </>
  );
}
