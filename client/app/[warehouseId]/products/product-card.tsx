import PackageIcon from "@/components/icons/package";
import {
    Card,
    CardContent,
    CardDescription,
    CardFooter,
    CardHeader,
    CardTitle,
} from "@/components/ui/card";

import Link from "next/link";

export default function ProductCard({ product }: { product: Product }) {
    return (
        <Card className="max-w-[480px] my-2">
            <Link href={`products/${product.id}`}>
                <CardHeader className="h-24">
                    <CardTitle>{product.name}</CardTitle>
                    <CardDescription>{product.description}</CardDescription>
                </CardHeader>
                <CardContent className="h-42">
                    {/*<div>{product.imageUrl}</div>*/}
                    <div className="flex justify-center">
                        <PackageIcon />
                    </div>
                    <ProductCardInfo
                        stock={product.stock}
                        netPrice={product.netPriceInCents}
                        grossPrice={product.grossPriceInCents}
                    />
                </CardContent>
                <CardFooter className="flex justify-end items-end text-sm text-slate-500">
                    {product.barcode}
                </CardFooter>
            </Link>
        </Card>
    );
}

function ProductCardInfo({
    stock,
    netPrice,
    grossPrice,
}: {
    stock: number;
    netPrice: number;
    grossPrice: number;
}) {
    return (
        <div className="flex flex-wrap justify-between">
            <div className="content-center">
                <p className="inline font-bold h-[100%]">{stock}</p> w magazynie
            </div>
            <div className="flex flex-wrap justify-end">
                <p className="w-full text-end font-bold text-xl">
                    {(netPrice / 100).toFixed(2)}$
                </p>
                <p className="w-full text-end font-light text-sm text-slate-500">
                    {(grossPrice / 100).toFixed(2)}$ z VAT
                </p>
            </div>
        </div>
    );
}
