"use client";

import { useSearchParams } from "next/navigation";
import ProductCard from "./product-card";

export default function FilterableProducts({
    products,
}: {
    products: Product[];
}) {
    const searchParams = useSearchParams();
    const subcategory = searchParams.get("subcategory");

    const filteredProducts = subcategory
        ? products.filter((product) => product.subcategory.id === subcategory)
        : products;

    return (
        <>
            {filteredProducts.length === 0 ? (
                <div className="w-full text-center mt-16 text-zinc-700">
                    Nie znaleziono produktów
                </div>
            ) : (
                <div className="grid grid-cols-3 m-4 justify-evenly">
                    {filteredProducts.map((product) => (
                        <ProductCard key={product.id} product={product} />
                    ))}
                </div>
            )}
        </>
    );
}
