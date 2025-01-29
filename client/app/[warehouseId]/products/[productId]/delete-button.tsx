"use client";

import { deleteProductAction } from "@/app/actions";
import { Alert, AlertDescription, AlertTitle } from "@/components/ui/alert";
import {
    AlertDialog,
    AlertDialogAction,
    AlertDialogCancel,
    AlertDialogContent,
    AlertDialogDescription,
    AlertDialogFooter,
    AlertDialogHeader,
    AlertDialogTitle,
    AlertDialogTrigger,
} from "@/components/ui/alert-dialog";
import { Button } from "@/components/ui/button";
import { useRouter } from "next/navigation";
import { useState } from "react";

export default function DeleteButton({ productId }: { productId: string }) {
    const [status, setStatus] = useState<"success" | "error" | null>(null);
    const router = useRouter();

    const handleDelete = async () => {
        try {
            deleteProductAction(productId);
            setStatus("success");

            setTimeout(() => {
                const currentUrl = window.location.href;
                const baseUrl = currentUrl.split("/").slice(0, -1).join("/");
                router.push(`${baseUrl}`);
            }, 2000);
        } catch (error) {
            console.error("Error deleting product:", error);
            setStatus("error");
        }
    };

    return (
        <div className="mt-20">
            <AlertDialog>
                <AlertDialogTrigger asChild>
                    <Button variant="destructive">
                        Usuń produkt z magazynu
                    </Button>
                </AlertDialogTrigger>
                <AlertDialogContent>
                    <AlertDialogHeader>
                        <AlertDialogTitle>
                            Czy na pewno chcesz usunąć produkt?
                        </AlertDialogTitle>
                        <AlertDialogDescription>
                            Tej akcji nie można cofnąć. Spowoduje trwałe
                            usunięcie produktu z bazy.
                        </AlertDialogDescription>
                    </AlertDialogHeader>
                    <AlertDialogFooter>
                        <AlertDialogCancel>Anuluj</AlertDialogCancel>
                        <AlertDialogAction
                            className="bg-red-500 hover:bg-red-400"
                            onClick={() => handleDelete()}
                        >
                            Usuń
                        </AlertDialogAction>
                    </AlertDialogFooter>
                </AlertDialogContent>
            </AlertDialog>

            {status === "success" && (
                <Alert className="mt-4 bg-green-100 border-green-500 text-green-700">
                    <AlertTitle>✅ Produkt usunięty</AlertTitle>
                    <AlertDescription>Przekierowywanie...</AlertDescription>
                </Alert>
            )}

            {status === "error" && (
                <Alert className="mt-4 bg-red-100 border-red-500 text-red-700">
                    <AlertTitle>❌ Błąd</AlertTitle>
                    <AlertDescription>Wystąpił problem.</AlertDescription>
                </Alert>
            )}
        </div>
    );
}
