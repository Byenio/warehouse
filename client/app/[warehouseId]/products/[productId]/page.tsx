import { PackageIconLarge } from "@/components/icons/package";
import { Button } from "@/components/ui/button";
import {
    Card,
    CardContent,
    CardDescription,
    CardFooter,
    CardHeader,
    CardTitle,
} from "@/components/ui/card";
import {
    Tooltip,
    TooltipContent,
    TooltipProvider,
    TooltipTrigger,
} from "@/components/ui/tooltip";
import Link from "next/link";
import { env } from "process";
import DeleteButton from "./delete-button";

export default async function Product({
    params,
}: {
    params: Promise<{ productId: string }>;
}) {
    const { productId } = await params;

    const data = await fetch(
        `http://${env.SERVER_ADDRESS}:${env.SERVER_PORT}/api/product/${productId}`
    );
    const product: Product = await data.json();

    return (
        <Card className="py-8 mx-32 my-12">
            <CardHeader>
                <CardTitle>{product.name}</CardTitle>
                <CardDescription>{product.description}</CardDescription>
            </CardHeader>
            <CardContent className="flex">
                <div className="basis-1/2 h-full flex justify-center align-middle">
                    <PackageIconLarge />
                </div>
                <div className="basis-1/2">
                    <ProductCardInfo product={product} />
                </div>
            </CardContent>
            <CardFooter className="flex justify-end items-end text-sm text-slate-500">
                Kod produktu: {product.barcode}
            </CardFooter>
        </Card>
    );
}

function ProductCardInfo({ product }: { product: Product }) {
    return (
        <div className="flex flex-wrap justify-between">
            <div className="basis-1/2">
                <CategoryInfo subcategory={product.subcategory} />
                <ManufacturerInfo manufacturer={product.manufacturer} />
            </div>
            <div className="basis-1/2 flex flex-wrap justify-end">
                <PriceInfo
                    netPrice={product.netPriceInCents}
                    grossPrice={product.grossPriceInCents}
                />
                <StockInfo stock={product.stock} />
                <DeleteButton productId={product.id} />
            </div>
        </div>
    );
}

function StockInfo({ stock }: { stock: number }) {
    return (
        <div>
            <b>{stock}</b> sztuk w magazynie
        </div>
    );
}

function CategoryInfo({ subcategory }: { subcategory: SubcategoryInfo }) {
    return (
        <div>
            <TooltipProvider delayDuration={200}>
                <Tooltip>
                    <TooltipTrigger className="cursor-default">
                        <div>
                            Kategoria <b>{subcategory.name}</b>
                        </div>
                    </TooltipTrigger>
                    <TooltipContent>
                        <p>{subcategory.description}</p>
                    </TooltipContent>
                </Tooltip>
            </TooltipProvider>
        </div>
    );
}

function ManufacturerInfo({
    manufacturer,
}: {
    manufacturer: ManufacturerInfo;
}) {
    return (
        <div>
            <div>
                Kraj pochodzenia <b>{manufacturer.country.name}</b>{" "}
                <p className="inline text-zinc-400">
                    #{manufacturer.country.code}
                </p>
            </div>
            <TooltipProvider delayDuration={200}>
                <Tooltip>
                    <TooltipTrigger className="cursor-default">
                        <div>
                            Producent <b>{manufacturer.name}</b>{" "}
                            <p className="inline text-zinc-400">
                                #{manufacturer.code}
                            </p>
                        </div>
                    </TooltipTrigger>
                    <TooltipContent>
                        <p>{manufacturer.description}</p>
                    </TooltipContent>
                </Tooltip>
            </TooltipProvider>
            <div>
                <Button variant="link" asChild>
                    <Link href={manufacturer.website}>
                        {manufacturer.website}
                    </Link>
                </Button>
            </div>
        </div>
    );
}

function PriceInfo({
    netPrice,
    grossPrice,
}: {
    netPrice: number;
    grossPrice: number;
}) {
    return (
        <>
            <p className="w-full text-end font-bold text-xl">
                {(netPrice / 100).toFixed(2)}$
            </p>
            <p className="w-full text-end font-light text-sm text-slate-500">
                {(grossPrice / 100).toFixed(2)}$ z VAT
            </p>
        </>
    );
}
