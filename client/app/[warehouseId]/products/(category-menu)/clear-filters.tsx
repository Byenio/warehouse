"use client";

import { Button } from "@/components/ui/button";
import { useRouter, useSearchParams } from "next/navigation";

export default function ClearFilters({ warehouseId }: { warehouseId: string }) {
    const router = useRouter();
    const searchParams = useSearchParams();

    const handleClearFilters = () => {
        const params = new URLSearchParams(searchParams.toString());
        params.delete("subcategory");
        router.push(`/${warehouseId}/products?${params.toString()}`);
    };

    return (
        <>
            <Button
                variant="link"
                className="w-full"
                onClick={() => handleClearFilters()}
            >
                Wyczyść filtry
            </Button>
        </>
    );
}
