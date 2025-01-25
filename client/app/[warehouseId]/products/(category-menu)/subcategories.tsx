"use client";

import { Button } from "@/components/ui/button";
import { useRouter, useSearchParams } from "next/navigation";

export default function Subcategories({
    subcategories,
}: {
    subcategories: SubcategoryInfo[];
}) {
    const router = useRouter();
    const searchParams = useSearchParams();

    const handleSubcategoryClick = (subcategoryId: string) => {
        const params = new URLSearchParams(searchParams.toString());
        params.set("subcategory", subcategoryId);
        router.push(`products?${params.toString()}`);
    };

    return (
        <>
            {subcategories.map((subcategory) => (
                <Button
                    key={subcategory.id}
                    variant="link"
                    className="w-full"
                    onClick={() => handleSubcategoryClick(subcategory.id)}
                >
                    {subcategory.name}
                </Button>
            ))}
        </>
    );
}
